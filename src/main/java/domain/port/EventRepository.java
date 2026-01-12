package domain.port;

import domain.model.Event;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository {
    // Zum Speichern des neu erstellten Events
    void save(Event event);

    // Für spätere Use Cases (UC2), aber gut, es schon im Kopf zu haben
    Optional<Event> findById(UUID id);
    List<Event> findAll();

    List<Event> findByCreatorId(UUID creatorId);

    void delete(UUID id);
}