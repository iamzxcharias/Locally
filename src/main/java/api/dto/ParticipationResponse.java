package api.dto;

import domain.model.ParticipationStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public class ParticipationResponse {
    public UUID id;
    public UUID userId;
    public UUID eventId;
    public ParticipationStatus status;
    public LocalDateTime createdAt;

    public ParticipationResponse(UUID id, UUID userId, UUID eventId, ParticipationStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.status = status;
        this.createdAt = createdAt;
    }
}
