package domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Friendship {

    // Da Friendship im UML keine eigene ID hat, nutzen wir oft eine zusammengesetzte ID
    // oder fügen eine technische ID hinzu. Das UML zeigt aber keine ID im Kasten[cite: 32].
    // Wir fügen eine ID hinzu, um konsistent mit JPA später zu sein, oder nutzen die User-Paare.
    // Hier entscheiden wir uns für eine technische ID zur einfacheren Handhabung.
    private final UUID id;

    private final UUID requesterId;
    private final UUID addresseeId;
    private final LocalDateTime createdAt;
    private final FriendshipStatus status; // z.B. "Accepted"

    public Friendship(UUID requesterId, UUID addresseeId, FriendshipStatus status) {
        validate(requesterId, addresseeId, status);

        this.id = UUID.randomUUID();
        this.requesterId = requesterId;
        this.addresseeId = addresseeId;
        this.createdAt = LocalDateTime.now();
        this.status = status;
    }

    //für Erstellung von Objekt-Kopien mit Wither-Methode
    private Friendship(UUID id, UUID requesterId, UUID addresseeId, FriendshipStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.requesterId = requesterId;
        this.addresseeId = addresseeId;
        this.status = status;
        this.createdAt = createdAt;

    }

    private void validate(UUID requesterId, UUID addresseeId, FriendshipStatus status) {
        if (requesterId == null || addresseeId == null) {
            throw new IllegalArgumentException("Friendship requires two users.");
        }
        if (requesterId.equals(addresseeId)) {
            throw new IllegalArgumentException("A user cannot be friends with themselves."); //
        }
        if (status == null) {
            throw new IllegalArgumentException("Friendship status is required.");
        }
    }

    public Friendship withStatus(FriendshipStatus newStatus) {
        if (this.status == newStatus) {
            return this; //keine Änderungen
        }
        // Wir geben eine NEUE Instanz zurück, aber mit der ALTEN ID!
        return new Friendship(this.id, this.requesterId, this.addresseeId, newStatus, this.createdAt);
    }

    // Getter
    public UUID getId() { return id; }
    public UUID getRequesterId() { return requesterId; }
    public UUID getAddresseeId() { return addresseeId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public FriendshipStatus getStatus() { return status; }
}