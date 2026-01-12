package persistence.entity;

import domain.model.ParticipationStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "participations")
public class ParticipationJpaEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "event_id", nullable = false)
    private UUID eventId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ParticipationStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // Standard-Konstruktor
    protected ParticipationJpaEntity() {
    }

    // Konstruktor
    public ParticipationJpaEntity(UUID id, UUID userId, UUID eventId, ParticipationStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getter
    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public UUID getEventId() { return eventId; }
    public ParticipationStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
