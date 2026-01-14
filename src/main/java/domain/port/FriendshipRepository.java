package domain.port;

import domain.model.Friendship;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FriendshipRepository {
    void save(Friendship friendship);
    Optional<Friendship> findById(UUID id);

    boolean existsById(UUID id);
    void delete(UUID id);
    List<Friendship> findByUserId(UUID userId);

    boolean existsByRequesterAndAddressee(UUID requesterId, UUID addresseeId);
}