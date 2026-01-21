package persistence.adapter;

import domain.model.Friendship;
import domain.model.FriendshipStatus;
import domain.port.FriendshipRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import persistence.entity.FriendshipJpaEntity;
import persistence.mapper.FriendshipMapper;
import persistence.repository.FriendshipJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class FriendshipPersistenceAdapter implements FriendshipRepository {

    @Inject
    FriendshipJpaRepository friendshipJpaRepository;

    @Inject
    FriendshipMapper friendshipMapper;

    @Override
    public void save(Friendship friendship) {
        FriendshipJpaEntity entity = friendshipMapper.toEntity(friendship);
        friendshipJpaRepository.getEntityManager().merge(entity);
    }

    @Override
    public Optional<Friendship> findById(UUID id) {
        return friendshipJpaRepository.findByIdOptional(id)
                .map(friendshipMapper::toDomain);
    }

    // FIX: Die Methode, die gefehlt hat
    @Override
    public boolean existsById(UUID id) {
        return friendshipJpaRepository.findByIdOptional(id).isPresent();
    }

    // FIX: Die Lösch-Methode
    @Override
    public void delete(UUID id) {
        friendshipJpaRepository.deleteById(id);
    }

    @Override
    public List<Friendship> findByUserId(UUID userId) {
        return friendshipJpaRepository.findByUserId(userId).stream()
                .map(friendshipMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByRequesterAndAddressee(UUID requesterId, UUID addresseeId) {
        // Nutzt die Panache-Logik, um zu prüfen ob eine Kombination existiert
        return friendshipJpaRepository.count("requesterId = ?1 and addresseeId = ?2", requesterId, addresseeId) > 0;
    }

    @Override
    public List<Friendship> searchForUser(UUID userId, FriendshipStatus status, UUID friendId, String friendQ, int page, int size) {
        return friendshipJpaRepository.searchForUser(userId, status, friendId, friendQ, page, size).stream()
                .map(friendshipMapper::toDomain)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public long countSearchForUser(UUID userId, FriendshipStatus status, UUID friendId, String friendQ) {
        return friendshipJpaRepository.countSearchForUser(userId, status, friendId, friendQ);
    }
    @Override
    public boolean areFriends(UUID userA, UUID userB) {
        // Wir prüfen, ob eine Freundschaft (A->B oder B->A) mit Status ACCEPTED existiert
        String query = "SELECT count(f) FROM FriendshipJpaEntity f " +
                "WHERE ((f.requesterId = :a AND f.addresseeId = :b) " +
                "OR (f.requesterId = :b AND f.addresseeId = :a)) " +
                "AND f.status = 'ACCEPTED'";

        Long count = friendshipJpaRepository.getEntityManager().createQuery(query, Long.class)
                .setParameter("a", userA)
                .setParameter("b", userB)
                .getSingleResult();

        return count > 0;
    }
}