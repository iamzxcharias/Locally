package persistence.mapper;

import domain.model.Friendship;
import jakarta.enterprise.context.ApplicationScoped;
import persistence.entity.FriendshipJpaEntity;

@ApplicationScoped
public class FriendshipMapper {

    // Von Domain (Business-Logik) zu Entity (Datenbank)
    public FriendshipJpaEntity toEntity(Friendship friendship) {
        return new FriendshipJpaEntity(
                friendship.getId(),
                friendship.getRequesterId(),
                friendship.getAddresseeId(),
                friendship.getCreatedAt(),
                friendship.getStatus()
        );
    }

    // Von Entity (Datenbank) zurück zur Domain (Business-Logik)
    public Friendship toDomain(FriendshipJpaEntity entity) {
        // ACHTUNG: Der 5-Argumente-Konstruktor in Friendship.java muss PUBLIC sein!
        return new Friendship(
                entity.getId(),
                entity.getRequesterId(),
                entity.getAddresseeId(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}