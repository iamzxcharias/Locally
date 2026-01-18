package persistence.adapter;

import domain.model.Participation;
import domain.model.ParticipationStatus;
import domain.port.ParticipationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import persistence.entity.ParticipationJpaEntity;
import persistence.mapper.ParticipationMapper;
import persistence.repository.ParticipationJpaRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

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
        return participationJpaRepository
                .find("userId = ?1 and eventId = ?2", userId, eventId)
                .firstResultOptional()
                .map(participationMapper::toDomain);
    }

    @Override
    public void delete(UUID id) {
        participationJpaRepository.deleteById(id);
    }

    @Override
    public long countByEventId(UUID eventId) {
        return participationJpaRepository.count("eventId", eventId);
    }

    @Override
    public List<Participation> search(UUID userId, UUID eventId, ParticipationStatus status, int page, int size) {
        return participationJpaRepository.search(userId, eventId, status, page, size).stream()
                .map(participationMapper::toDomain)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public long countSearch(UUID userId, UUID eventId, ParticipationStatus status) {
        return participationJpaRepository.countSearch(userId, eventId, status);
    }
}