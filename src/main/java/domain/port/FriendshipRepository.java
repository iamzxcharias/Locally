package domain.port;

import domain.model.*;
import java.util.*;

public interface FriendshipRepository {
    void save(Friendship friendship);

    Optional<Friendship> findById(UUID id);
    List<Friendship> findByUserId(UUID userId);

    boolean existsById(UUID id);

    void delete(UUID id);

    boolean existsByRequesterAndAddressee(UUID requesterId, UUID addresseeId);
    
    List<Friendship> searchForUser(UUID userId, FriendshipStatus status, UUID friendId, String friendQ, int page, int size);
    long countSearchForUser(UUID userId, FriendshipStatus status, UUID friendId, String friendQ);
}