package api.dto;

import domain.model.FriendshipStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public class FriendshipResponse {
    public UUID id;
    public UUID requesterId;
    public UUID addresseeId;
    public FriendshipStatus status;
    public LocalDateTime createdAt;

    public FriendshipResponse(UUID id, UUID requesterId, UUID addresseeId, FriendshipStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.requesterId = requesterId;
        this.addresseeId = addresseeId;
        this.status = status;
        this.createdAt = createdAt;
    }
}
