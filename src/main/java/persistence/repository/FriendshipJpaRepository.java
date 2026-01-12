package persistence.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import persistence.entity.FriendshipJpaEntity;
import java.util.UUID;
import java.util.List;

@ApplicationScoped
public class FriendshipJpaRepository implements PanacheRepositoryBase<FriendshipJpaEntity, UUID> {
    public List<FriendshipJpaEntity> findFriends(UUID userId) {
        return list("requesterId = ?1 or addresseeId = ?1", userId);
    }
}
