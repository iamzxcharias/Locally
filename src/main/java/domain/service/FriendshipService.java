package domain.service;

import domain.model.Friendship;
import domain.model.FriendshipStatus;
import domain.port.FriendshipRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;

    @Inject
    public FriendshipService(FriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }


    public List<Friendship> getFriendshipsForUser(UUID userId) {
        return friendshipRepository.findByUserId(userId);
    }

    public Friendship getFriendshipById(UUID id) {
        return friendshipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Friendship not found with id: " + id));
    }


    @Transactional
    public Friendship requestFriendship(UUID requesterId, UUID addresseeId) {
        if (requesterId.equals(addresseeId)) {
            throw new IllegalArgumentException("You cannot be friends with yourself.");
        }

        if (friendshipRepository.existsByRequesterAndAddressee(requesterId, addresseeId)) {
            throw new IllegalArgumentException("A friendship request already exists.");
        }

        Friendship friendship = new Friendship(requesterId, addresseeId);

        friendshipRepository.save(friendship);
        return friendship;
    }


    @Transactional
    public Friendship acceptFriendship(UUID friendshipId) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new RuntimeException("Friendship request not found."));

        Friendship acceptedFriendship = friendship.accept();

        friendshipRepository.save(acceptedFriendship);

        return acceptedFriendship;
    }

    @Transactional
    public void deleteFriendship(UUID id) {
        // Wir prüfen erst, ob sie existiert, um eine saubere Fehlermeldung zu geben
        if (!friendshipRepository.existsById(id)) {
            throw new RuntimeException("Friendship not found with id: " + id);
        }
        friendshipRepository.delete(id);
    }

    public List<Friendship> searchFriendships(UUID userId, FriendshipStatus status, UUID friendId, String friendQ, int page, int size) {
        return friendshipRepository.searchForUser(userId, status, friendId, friendQ, page, size);
    }

    public long countSearchFriendships(UUID userId, FriendshipStatus status, UUID friendId, String friendQ) {
        return friendshipRepository.countSearchForUser(userId, status, friendId, friendQ);
    }
}
