package api.dto;

import domain.model.FriendshipStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public class InvitationRequest {
    public UUID eventId;
    public UUID targetUserId;
}
