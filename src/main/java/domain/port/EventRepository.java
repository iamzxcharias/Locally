package domain.port;

import domain.model.Event;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository {
    void save(Event event);

    Optional<Event> findById(UUID id);
    List<Event> findAll();

    List<Event> findByCreatorId(UUID creatorId);

    void delete(UUID id);
}