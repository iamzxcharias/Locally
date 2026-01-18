package persistence.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import persistence.entity.UserJpaEntity;
import java.util.UUID;
import java.util.Optional;
import io.quarkus.panache.common.Page;
import java.util.*;

@ApplicationScoped // Wichtig für Quarkus (Dependency Injection)
public class UserJpaRepository implements PanacheRepositoryBase<UserJpaEntity, UUID> {
    public Optional<UserJpaEntity> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }

public List<UserJpaEntity> search(String q, int page, int size) {
    String query = "1=1";
    Map<String, Object> params = new HashMap<>();

    if (q != null && !q.isBlank()) {
        query += " and (lower(name) like :q or lower(email) like :q)";
        params.put("q", "%" + q.toLowerCase() + "%");
    }

    return find(query + " order by name asc, id asc", params)
            .page(Page.of(page, size))
            .list();
}

public long countSearch(String q) {
    String query = "1=1";
    Map<String, Object> params = new HashMap<>();

    if (q != null && !q.isBlank()) {
        query += " and (lower(name) like :q or lower(email) like :q)";
        params.put("q", "%" + q.toLowerCase() + "%");
    }

    return find(query, params).count();
}
}