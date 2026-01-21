package domain.port;

import domain.model.Friendship;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import domain.model.FriendshipStatus;

public interface FriendshipRepository {
    void save(Friendship friendship);
    Optional<Friendship> findById(UUID id);

    boolean existsById(UUID id);
    void delete(UUID id);
    List<Friendship> findByUserId(UUID userId);

    boolean existsByRequesterAndAddressee(UUID requesterId, UUID addresseeId);
    
    List<Friendship> searchForUser(UUID userId, FriendshipStatus status, UUID friendId, String friendQ, int page, int size);
    long countSearchForUser(UUID userId, FriendshipStatus status, UUID friendId, String friendQ);

    boolean areFriends(UUID userA, UUID userB);
}