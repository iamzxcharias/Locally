package domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Participation {

    private final UUID id;
    private final UUID userId;
    private final UUID eventId;
    private ParticipationStatus status;
    private final LocalDateTime createdAt;

    public Participation(UUID userId, UUID eventId, ParticipationStatus status) {
        this(UUID.randomUUID(), userId, eventId, status, LocalDateTime.now());
    }

    public Participation(UUID id, UUID userId, UUID eventId, ParticipationStatus status, LocalDateTime createdAt) {
        validate(userId, eventId, status);
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

    public Participation withStatus(ParticipationStatus newStatus) {
        if (this.status == newStatus) return this;
        return new Participation(this.id, this.userId, this.eventId, newStatus, this.createdAt);
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public UUID getEventId() { return eventId; }
    public ParticipationStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setStatus(ParticipationStatus status) {
        this.status = status;
    }
}