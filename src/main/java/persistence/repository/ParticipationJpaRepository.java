package persistence.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import persistence.entity.ParticipationJpaEntity;
import java.util.UUID;
import java.util.List;

@ApplicationScoped
public class ParticipationJpaRepository implements PanacheRepositoryBase<ParticipationJpaEntity, UUID> {
    public List<ParticipationJpaEntity> findByEventId(UUID eventId) {
        return list("eventId", eventId);
    }
}
