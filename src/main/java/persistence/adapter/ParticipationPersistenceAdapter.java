package persistence.adapter;

import domain.model.Participation;
import domain.port.ParticipationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import persistence.entity.ParticipationJpaEntity;
import persistence.mapper.ParticipationMapper;
import persistence.repository.ParticipationJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class ParticipationPersistenceAdapter implements ParticipationRepository {

    @Inject
    ParticipationJpaRepository participationJpaRepository;

    @Inject
    ParticipationMapper participationMapper;

    @Override
    public void save(Participation participation) {
        ParticipationJpaEntity entity = participationMapper.toEntity(participation);
        participationJpaRepository.getEntityManager().merge(entity);
    }

    @Override
    public Optional<Participation> findById(UUID id) {
        return participationJpaRepository.findByIdOptional(id)
                .map(participationMapper::toDomain);
    }

    @Override
    public Optional<Participation> findByUserIdAndEventId(UUID userId, UUID eventId) {
        // Nutzt Panache-Query: find("userId = ?1 and eventId = ?2", ...)
        return participationJpaRepository.find("userId = ?1 and eventId = ?2", userId, eventId)
                .firstResultOptional()
                .map(participationMapper::toDomain);
    }

    @Override
    public boolean existsByUserIdAndEventId(UUID userId, UUID eventId) {
        return participationJpaRepository.find("userId = ?1 and eventId = ?2", userId, eventId)
                .count() > 0;
    }

    @Override
    public List<Participation> findByEventId(UUID eventId) {
        return participationJpaRepository.findByEventId(eventId).stream()
                .map(participationMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID userId, UUID eventId) {
        participationJpaRepository.delete("userId = ?1 and eventId = ?2", userId, eventId);
    }
}