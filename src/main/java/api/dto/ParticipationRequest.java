package api.dto;

import domain.model.ParticipationStatus;
import java.util.UUID;

public class ParticipationRequest {
    public UUID userId;
    public UUID eventId;
    public ParticipationStatus status; // GOING, INTERESTED, etc.

    public ParticipationRequest() {}
}