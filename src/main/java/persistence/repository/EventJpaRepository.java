package persistence.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.panache.common.Page;
import persistence.entity.EventJpaEntity;
import java.util.UUID;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.HashMap;

@ApplicationScoped
public class EventJpaRepository implements PanacheRepositoryBase<EventJpaEntity, UUID> {
    public List<EventJpaEntity> findByCreatorId(UUID creatorId) {
        return list("creatorId", creatorId);
    }
 public List<EventJpaEntity> search(String q, String category, LocalDateTime from, LocalDateTime to, int page, int size) {
        QueryParts parts = buildQuery(q, category, from, to);
        return find(parts.query + " order by startsAt asc", parts.params)
                .page(Page.of(page, size))
                .list();
    }

    public long countSearch(String q, String category, LocalDateTime from, LocalDateTime to) {
        QueryParts parts = buildQuery(q, category, from, to);
        return find(parts.query, parts.params).count();
    }

    private QueryParts buildQuery(String q, String category, LocalDateTime from, LocalDateTime to) {
        StringBuilder query = new StringBuilder("1=1");
        Map<String, Object> params = new HashMap<>();

        if (category != null && !category.isBlank()) {
            query.append(" and category = :category");
            params.put("category", category);
        }

        if (q != null && !q.isBlank()) {
            query.append(" and (lower(title) like :q or lower(coalesce(description, '')) like :q)");
            params.put("q", "%" + q.toLowerCase() + "%");
        }

        if (from != null) {
            query.append(" and startsAt >= :from");
            params.put("from", from);
        }

        if (to != null) {
            query.append(" and startsAt <= :to");
            params.put("to", to);
        }

        return new QueryParts(query.toString(), params);
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
