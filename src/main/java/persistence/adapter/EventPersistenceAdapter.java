package persistence.adapter;

import domain.model.Event;
import domain.port.EventRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import persistence.entity.EventJpaEntity;
import persistence.mapper.EventMapper;
import persistence.repository.EventJpaRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class EventPersistenceAdapter implements EventRepository {

    @Inject
    EventJpaRepository eventJpaRepository;

    @Inject
    EventMapper eventMapper;

    @Override
    public void save(Event event) {
        EventJpaEntity entity = eventMapper.toEntity(event);
        eventJpaRepository.getEntityManager().merge(entity);
    }

    @Override
    public Optional<Event> findById(UUID id) {
        return eventJpaRepository.findByIdOptional(id)
                .map(eventMapper::toDomain);
    }

    @Override
    public List<Event> findAll() {
        return eventJpaRepository.listAll().stream()
                .map(eventMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> findByCreatorId(UUID creatorId) {
        return eventJpaRepository.findByCreatorId(creatorId).stream()
                .map(eventMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        eventJpaRepository.deleteById(id);
    }

    @Override
    public List<Event> search(String q, String category, LocalDateTime from, LocalDateTime to, int page, int size) {
        return eventJpaRepository.search(q, category, from, to, page, size).stream()
                .map(eventMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public long countSearch(String q, String category, LocalDateTime from, LocalDateTime to) {
        return eventJpaRepository.countSearch(q, category, from, to);
    }

    @Override
    public void updateParticipantCount(UUID eventId, int participantCount) {
        eventJpaRepository.findByIdOptional(eventId).ifPresent(entity -> {
            entity.setParticipantCount(participantCount);
            eventJpaRepository.getEntityManager().merge(entity);
        });
    }
}