package domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Friendship {
    private final UUID id;
    private final UUID requesterId;
    private final UUID addresseeId;
    private final FriendshipStatus status;
    private final LocalDateTime createdAt;

    public Friendship(UUID requesterId, UUID addresseeId) {
        this(UUID.randomUUID(), requesterId, addresseeId, FriendshipStatus.PENDING, LocalDateTime.now());
    }

    public Friendship(UUID id, UUID requesterId, UUID addresseeId, FriendshipStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.requesterId = requesterId;
        this.addresseeId = addresseeId;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Friendship accept() {
        if (this.status != FriendshipStatus.PENDING) {
            throw new IllegalStateException("Only pending requests can be accepted.");
        }
        return new Friendship(this.id, this.requesterId, this.addresseeId, FriendshipStatus.ACCEPTED, this.createdAt);
    }

    public UUID getId() { return id; }
    public UUID getRequesterId() { return requesterId; }
    public UUID getAddresseeId() { return addresseeId; }
    public FriendshipStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}