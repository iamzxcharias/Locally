package persistence.repository;

import domain.model.ParticipationStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import persistence.entity.ParticipationJpaEntity;

import java.util.*;

@ApplicationScoped
public class ParticipationJpaRepository implements PanacheRepositoryBase<ParticipationJpaEntity, UUID> {

    public List<ParticipationJpaEntity> findByEventId(UUID eventId) {
        return list("eventId", eventId);
    }

    public List<ParticipationJpaEntity> search(UUID userId, UUID eventId, ParticipationStatus status, int page, int size) {
        QueryParts parts = buildQuery(userId, eventId, status);
        return find(parts.query() + " order by createdAt desc, id asc", parts.params())
                .page(Page.of(page, size))
                .list();
    }

    public long countSearch(UUID userId, UUID eventId, ParticipationStatus status) {
        QueryParts parts = buildQuery(userId, eventId, status);
        return find(parts.query(), parts.params()).count();
    }

    private QueryParts buildQuery(UUID userId, UUID eventId, ParticipationStatus status) {
        StringBuilder q = new StringBuilder("1=1");
        Map<String, Object> p = new HashMap<>();

        if (userId != null) {
            q.append(" and userId = :userId");
            p.put("userId", userId);
        }
        if (eventId != null) {
            q.append(" and eventId = :eventId");
            p.put("eventId", eventId);
        }
        if (status != null) {
            q.append(" and status = :status");
            p.put("status", status);
        }

        return new QueryParts(q.toString(), p);
    }

    private record QueryParts(String query, Map<String, Object> params) { }
}
