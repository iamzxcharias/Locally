package persistence.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import persistence.entity.EventJpaEntity;
import java.util.UUID;
import java.util.List;

@ApplicationScoped
public class EventJpaRepository implements PanacheRepositoryBase<EventJpaEntity, UUID> {
    public List<EventJpaEntity> findByCreatorId(UUID creatorId) {
        return list("creatorId", creatorId);
    }
}
