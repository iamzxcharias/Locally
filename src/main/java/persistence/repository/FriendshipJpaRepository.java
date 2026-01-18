package persistence.repository;

import domain.model.FriendshipStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import persistence.entity.FriendshipJpaEntity;
import persistence.entity.UserJpaEntity;

import java.util.*;

@ApplicationScoped
public class FriendshipJpaRepository implements PanacheRepositoryBase<FriendshipJpaEntity, UUID> {

    public List<FriendshipJpaEntity> findByUserId(UUID userId) {
        return list("requesterId = ?1 or addresseeId = ?1", userId);
    }

    public List<FriendshipJpaEntity> findByRequesterId(UUID requesterId) {
        return list("requesterId", requesterId);
    }

    public List<FriendshipJpaEntity> findByAddresseeId(UUID addresseeId) {
        return list("addresseeId", addresseeId);
    }

    public List<FriendshipJpaEntity> searchForUser(UUID userId, FriendshipStatus status, UUID friendId, String friendQ, int page, int size) {
    QueryParts parts = buildQuery(userId, status, friendId, friendQ);
        return find(parts.query + " order by f.createdAt desc, f.id asc", parts.params)
            .page(Page.of(page, size))
            .list();
    }

    public long countSearchForUser(UUID userId, FriendshipStatus status, UUID friendId, String friendQ) {
        QueryParts parts = buildQuery(userId, status, friendId, friendQ);
        return find(parts.query, parts.params).count();
    }

    private QueryParts buildQuery(UUID userId, FriendshipStatus status, UUID friendId, String friendQ) {
        StringBuilder q = new StringBuilder("from FriendshipJpaEntity f where 1=1");
        Map<String, Object> p = new HashMap<>();

        q.append(" and (f.requesterId = :userId or f.addresseeId = :userId)");
        p.put("userId", userId);

        if (status != null) {
            q.append(" and f.status = :status");
            p.put("status", status);
        }

        if (friendId != null) {
            q.append(" and ((f.requesterId = :userId and f.addresseeId = :friendId) or (f.addresseeId = :userId and f.requesterId = :friendId))");
            p.put("friendId", friendId);
        }

        if (friendQ != null && !friendQ.isBlank()) {
            q.append(" and exists (select 1 from UserJpaEntity u where " +
                    "((f.requesterId = :userId and u.id = f.addresseeId) or (f.addresseeId = :userId and u.id = f.requesterId))" +
                    " and (lower(u.name) like :fq or lower(u.email) like :fq))");
            p.put("fq", "%" + friendQ.toLowerCase() + "%");
        }

        return new QueryParts(q.toString(), p);
    }

    private static final class QueryParts {
        final String query;
        final Map<String, Object> params;
        QueryParts(String query, Map<String, Object> params) {
            this.query = query;
            this.params = params;
        }
    }
}
