package domain.port;

import domain.model.Participation;
import java.util.Optional;
import java.util.UUID;

public interface ParticipationRepository {

    //Speichert eine Partizipation persistent
    void save(Participation participation);

    //Sucht eine Partizipation anhand seiner ID
    Optional<Participation> findById(UUID id);

    // NEU: Wir müssen das Objekt laden können, um es zu updaten
    Optional<Participation> findByUserIdAndEventId(UUID userId, UUID eventId);

    // Prüft, ob eine Teilnahme für diesen User an diesem Event schon existiert
    // (Verhindert doppelte Anmeldungen gemäß UC3)
    boolean existsByUserIdAndEventId(UUID userId, UUID eventId);
}