package domain.port;

import domain.model.Friendship;
import java.util.Optional;
import java.util.UUID;

public interface FriendshipRepository {
    void save(Friendship friendship);
    Optional<Friendship> findById(UUID id);
    // Verhindert doppelte Anfragen
    boolean existsByRequesterAndAddressee(UUID requesterId, UUID addresseeId);
}