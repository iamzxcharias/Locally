package persistence.mapper;

import domain.model.Friendship;
import jakarta.enterprise.context.ApplicationScoped;
import persistence.entity.FriendshipJpaEntity;

@ApplicationScoped
public class FriendshipMapper {

    public FriendshipJpaEntity toEntity(Friendship friendship) {
        return new FriendshipJpaEntity(
                friendship.getId(),
                friendship.getRequesterId(),
                friendship.getAddresseeId(),
                friendship.getCreatedAt(),
                friendship.getStatus()
        );
    }

    public Friendship toDomain(FriendshipJpaEntity entity) {
        return new Friendship(
                entity.getId(),
                entity.getRequesterId(),
                entity.getAddresseeId(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}