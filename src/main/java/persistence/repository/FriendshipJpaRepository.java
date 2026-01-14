package persistence.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import persistence.entity.FriendshipJpaEntity;

import java.util.List;
import java.util.UUID;

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
}
