package persistence.adapter;

import domain.model.Friendship;
import domain.port.FriendshipRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import persistence.entity.FriendshipJpaEntity;
import persistence.mapper.FriendshipMapper;
import persistence.repository.FriendshipJpaRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class FriendshipPersistenceAdapter implements FriendshipRepository {

    @Inject
    FriendshipJpaRepository repository;

    @Inject
    FriendshipMapper mapper;

    @Override
    public void save(Friendship friendship) {
        repository.getEntityManager().merge(mapper.toEntity(friendship));
    }

    @Override
    public boolean existsByRequesterIdAndAddresseeId(UUID requesterId, UUID addresseeId) {
        return repository.find("requesterId = ?1 and addresseeId = ?2", requesterId, addresseeId)
                .count() > 0;
    }

    @Override
    public List<Friendship> findAllByRequesterIdOrAddresseeId(UUID requesterId, UUID addresseeId) {
        return repository.find("requesterId = ?1 or addresseeId = ?2", requesterId, addresseeId)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}