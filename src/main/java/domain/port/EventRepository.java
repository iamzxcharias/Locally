package domain.port;

import domain.model.Event;
import java.util.*;
import java.time.LocalDateTime;

public interface EventRepository {
    void save(Event event);

    Optional<Event> findById(UUID id);
    List<Event> findAll();
    List<Event> findByCreatorId(UUID creatorId);

    void delete(UUID id);

    List<Event> search(String q, String category, LocalDateTime from, LocalDateTime to, int page, int size);
    long countSearch(String q, String category, LocalDateTime from, LocalDateTime to);

    void updateParticipantCount(UUID eventId, int participantCount);
}