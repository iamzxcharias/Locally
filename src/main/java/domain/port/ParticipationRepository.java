package domain.port;

import domain.model.Participation;
import java.util.Optional;
import java.util.UUID;

public interface ParticipationRepository {
    void save(Participation participation);
    Optional<Participation> findById(UUID id);
    Optional<Participation> findByUserIdAndEventId(UUID userId, UUID eventId); // Wichtig für UC3!
    void delete(UUID id);
}