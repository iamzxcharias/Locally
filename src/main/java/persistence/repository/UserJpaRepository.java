package persistence.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import persistence.entity.UserJpaEntity;
import java.util.UUID;
import java.util.Optional;

@ApplicationScoped // Wichtig für Quarkus (Dependency Injection)
public class UserJpaRepository implements PanacheRepositoryBase<UserJpaEntity, UUID> {
    public Optional<UserJpaEntity> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }
}