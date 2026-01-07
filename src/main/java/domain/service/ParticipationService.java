package domain.service;

import domain.model.Participation;
import domain.model.ParticipationStatus;
import domain.port.EventRepository;
import domain.port.ParticipationRepository;
import domain.port.UserRepository;

import java.util.Optional;
import java.util.UUID;

public class ParticipationService {

    private final ParticipationRepository participationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    // Konstruktor Injection für alle benötigten Ports
    public ParticipationService(ParticipationRepository participationRepository,
                                EventRepository eventRepository,
                                UserRepository userRepository) {
        this.participationRepository = participationRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    /**
     * UC3: Register Participation
     * Ermöglicht es einem User, an einem Event teilzunehmen oder den Status zu ändern.
     */
    public Participation updateParticipation(UUID userId, UUID eventId, ParticipationStatus newStatus) {

        // 1. Validierung: Existieren User und Event?
        // Das PDF verlangt explizit: "System validates that the referenced event exists"
        if (eventRepository.findById(eventId).isEmpty()) {
            throw new IllegalArgumentException("Event not found.");
        }
        if (userRepository.findById(userId).isEmpty()) {
            throw new IllegalArgumentException("User not found.");
        }

        // 2. Prüfen, ob bereits eine Teilnahme existiert
        // Wir nutzen hier findById oder eine spezifische Suchmethode,
        // idealerweise bräuchten wir im Repo eine Methode: findByUserIdAndEventId.
        // Da wir aktuell nur 'existsByUserIdAndEventId' im Interface haben,
        // müssen wir das Interface 'ParticipationRepository' gleich kurz erweitern,
        // um das Objekt auch wirklich zu bekommen, nicht nur ein boolean.

        Optional<Participation> existingParticipation = participationRepository.findByUserIdAndEventId(userId, eventId);

        Participation participationToSave;

        if (existingParticipation.isPresent()) {
            // Fall B: Update (Status ändern)
            Participation oldParticipation = existingParticipation.get();

            // Wir nutzen die "Wither"-Methode für Immutability -> erzeugt neues Objekt mit neuer Status
            participationToSave = oldParticipation.withStatus(newStatus);

        } else {
            // Fall A: Neu anlegen
            participationToSave = new Participation(userId, eventId, newStatus);
        }

        // 3. Persistieren
        participationRepository.save(participationToSave);

        // 4. Rückgabe des aktuellen Stands
        return participationToSave;
    }
}