package persistence.entity;

import domain.model.FriendshipStatus;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "friendships")
public class FriendshipJpaEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "requester_id", nullable = false)
    private UUID requesterId;

    @Column(name = "addressee_id", nullable = false)
    private UUID addresseeId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FriendshipStatus status;

    // Standard-Konstruktor
    protected FriendshipJpaEntity() {
    }

    // Konstruktor
    public FriendshipJpaEntity(UUID id, UUID requesterId, UUID addresseeId, LocalDateTime createdAt, FriendshipStatus status) {
        this.id = id;
        this.requesterId = requesterId;
        this.addresseeId = addresseeId;
        this.createdAt = createdAt;
        this.status = status;
    }

    // Getter
    public UUID getId() { return id; }
    public UUID getRequesterId() { return requesterId; }
    public UUID getAddresseeId() { return addresseeId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public FriendshipStatus getStatus() { return status; }
}
