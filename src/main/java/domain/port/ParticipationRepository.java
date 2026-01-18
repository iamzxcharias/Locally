package domain.port;

import domain.model.Participation;
import domain.model.ParticipationStatus;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface ParticipationRepository {
    void save(Participation participation);
    Optional<Participation> findById(UUID id);
    Optional<Participation> findByUserIdAndEventId(UUID userId, UUID eventId); // Wichtig für UC3!
    void delete(UUID id);
    long countByEventId(UUID eventId);
    List<Participation> search(UUID userId, UUID eventId, ParticipationStatus status, int page, int size);
    long countSearch(UUID userId, UUID eventId, ParticipationStatus status);
}