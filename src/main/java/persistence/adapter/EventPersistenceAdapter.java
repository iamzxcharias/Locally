package persistence.adapter;

import domain.model.Event;
import domain.port.EventRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import persistence.entity.EventJpaEntity;
import persistence.mapper.EventMapper;
import persistence.repository.EventJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@ApplicationScoped
public class EventPersistenceAdapter implements EventRepository {

    @Inject
    EventJpaRepository eventJpaRepository;

    @Inject
    EventMapper eventMapper;

    @Override
    public void save(Event event) {
        // Wandelt das Domain-Objekt in eine JPA-Entity um
        EventJpaEntity entity = eventMapper.toEntity(event);
        // Speichert oder aktualisiert das Event in der Datenbank
        eventJpaRepository.getEntityManager().merge(entity);
    }

    @Override
    public Optional<Event> findById(UUID id) {
        // Holt die Entity und wandelt sie für die Domain zurück
        return eventJpaRepository.findByIdOptional(id)
                .map(eventMapper::toDomain);
    }

    @Override
    public List<Event> findAll() {
        // Mappt die gesamte Liste von Entities zu Domain-Modellen
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
}
