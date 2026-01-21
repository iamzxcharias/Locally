package persistence.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import persistence.entity.UserJpaEntity;

import java.util.*;

@ApplicationScoped
public class UserJpaRepository implements PanacheRepositoryBase<UserJpaEntity, UUID> {

    public Optional<UserJpaEntity> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }

    public List<UserJpaEntity> search(String q, int page, int size) {
        QueryParts parts = buildQuery(q);
        return find(parts.query() + " order by name asc, id asc", parts.params())
                .page(Page.of(page, size))
                .list();
    }

    public long countSearch(String q) {
        QueryParts parts = buildQuery(q);
        return find(parts.query(), parts.params()).count();
    }

    private QueryParts buildQuery(String q) {
        StringBuilder query = new StringBuilder("1=1");
        Map<String, Object> params = new HashMap<>();

        if (q != null && !q.isBlank()) {
            query.append(" and (lower(name) like :q or lower(email) like :q)");
            params.put("q", "%" + q.toLowerCase() + "%");
        }

        return new QueryParts(query.toString(), params);
    }

    private record QueryParts(String query, Map<String, Object> params) { }
}