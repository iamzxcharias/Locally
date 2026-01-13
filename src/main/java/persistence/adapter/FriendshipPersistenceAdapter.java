package persistence.adapter;

import domain.model.Friendship;
import domain.port.FriendshipRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import persistence.entity.FriendshipJpaEntity;
import persistence.mapper.FriendshipMapper;
import persistence.repository.FriendshipJpaRepository;

import java.util.Optional;
import java.util.UUID;

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

    @Override
    public boolean existsByRequesterAndAddressee(UUID requesterId, UUID addresseeId) {
        return friendshipJpaRepository
                .find("requesterId = ?1 and addresseeId = ?2", requesterId, addresseeId)
                .count() > 0;
    }
}