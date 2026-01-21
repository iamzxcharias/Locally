package persistence.entity;

import domain.model.FriendshipStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "friendships")
public class FriendshipJpaEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "requester_id", nullable = false)
    private UUID requesterId;

    @Column(name = "addressee_id", nullable = false)
    private UUID addresseeId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FriendshipStatus status;

    protected FriendshipJpaEntity() {
    }

    public FriendshipJpaEntity(UUID id, UUID requesterId, UUID addresseeId, LocalDateTime createdAt, FriendshipStatus status) {
        this.id = id;
        this.requesterId = requesterId;
        this.addresseeId = addresseeId;
        this.createdAt = createdAt;
        this.status = status;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getRequesterId() { return requesterId; }
    public void setRequesterId(UUID requesterId) { this.requesterId = requesterId; }

    public UUID getAddresseeId() { return addresseeId; }
    public void setAddresseeId(UUID addresseeId) { this.addresseeId = addresseeId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public FriendshipStatus getStatus() { return status; }
    public void setStatus(FriendshipStatus status) { this.status = status; }
}