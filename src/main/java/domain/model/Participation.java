package domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Participation {

    private final UUID id;
    private final UUID userId;
    private final UUID eventId;
    private final ParticipationStatus status; // Jetzt Enum statt String!
    private final LocalDateTime createdAt;

    // Konstruktor
    public Participation(UUID userId, UUID eventId, ParticipationStatus status) {
        validate(userId, eventId, status);

        this.id = UUID.randomUUID();
        this.userId = userId;
        this.eventId = eventId;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    // Privater Konstruktor für das "Kopieren" (für Immutability)
    // Wir übergeben hier die *bestehende* ID, damit die Identität erhalten bleibt.
    public Participation(UUID id, UUID userId, UUID eventId, ParticipationStatus status, LocalDateTime createdAt) {
        // Hier keine Validierung nötig, da wir Daten aus einem validen Objekt kopieren
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.status = status;
        this.createdAt = createdAt;
    }

    private void validate(UUID userId, UUID eventId, ParticipationStatus status) {
        if (userId == null) throw new IllegalArgumentException("User required.");
        if (eventId == null) throw new IllegalArgumentException("Event required.");
        if (status == null) throw new IllegalArgumentException("Status required.");
    }

    // Die "Wither"-Methode für Statusänderungen (UC3)
    public Participation withStatus(ParticipationStatus newStatus) {
        if (this.status == newStatus) {
            return this; // Keine Änderung nötig
        }
        // Wir geben eine NEUE Instanz zurück, aber mit der ALTEN ID!
        return new Participation(this.id, this.userId, this.eventId, newStatus, this.createdAt);
    }

    // Getter (angepasst auf Enum)
    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public UUID getEventId() { return eventId; }
    public ParticipationStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}