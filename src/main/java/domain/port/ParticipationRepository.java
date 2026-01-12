package domain.port;

import domain.model.Participation;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParticipationRepository {
    void save(Participation participation);

    Optional<Participation> findById(UUID id);

    // Diese müssen im Adapter implementiert werden:
    Optional<Participation> findByUserIdAndEventId(UUID userId, UUID eventId);

    boolean existsByUserIdAndEventId(UUID userId, UUID eventId);

    // Damit das @Override im Adapter funktioniert, müssen diese hier stehen:
    List<Participation> findByEventId(UUID eventId);

    void delete(UUID userId, UUID eventId);
}