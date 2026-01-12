package persistence.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import persistence.entity.FriendshipJpaEntity;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FriendshipJpaRepository implements PanacheRepositoryBase<FriendshipJpaEntity, UUID> {

    // Diese Methode hat im Test gefehlt:
    public List<FriendshipJpaEntity> findByRequesterId(UUID requesterId) {
        return list("requesterId", requesterId);
    }

    // Kleiner Bonus: Die wirst du später sicher auch für den Empfänger brauchen
    public List<FriendshipJpaEntity> findByAddresseeId(UUID addresseeId) {
        return list("addresseeId", addresseeId);
    }
}
