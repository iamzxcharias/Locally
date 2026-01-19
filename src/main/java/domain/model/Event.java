package domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Event {
    private UUID id;
    private String title;
    private String category;
    private String description;
    private LocalDateTime startsAt;
    private String placeName;
    private double lat;
    private double lng;
    private UUID creatorId;
    private int participantCount;

    // 1. KONSTRUKTOR: Für neue Events (9 Parameter)
    // Wird in deinen IntegrationTests und im Service genutzt.
    public Event(String title, String category, String description,
                 LocalDateTime startsAt, String placeName, double lat,
                 double lng, UUID creatorId) {

        validate(title, startsAt, lat, lng, creatorId); // Validierung vor Zuweisung

        this.id = UUID.randomUUID();
        this.title = title;
        this.category = category;
        this.description = description;
        this.startsAt = startsAt;
        this.placeName = placeName;
        this.lat = lat;
        this.lng = lng;
        this.creatorId = creatorId;
        this.participantCount = 0;
    }

    // 2. KONSTRUKTOR: Für den EventMapper (10 Parameter)
    // Wird genutzt, um existierende Events aus der DB zu laden.
    public Event(UUID id, String title, String category, String description,
                 LocalDateTime startsAt, String placeName, double lat,
                 double lng, UUID creatorId, int participantCount) {

        validate(title, startsAt, lat, lng, creatorId); // Validierung vor Zuweisung

        this.id = id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.startsAt = startsAt;
        this.placeName = placeName;
        this.lat = lat;
        this.lng = lng;
        this.creatorId = creatorId;
        this.participantCount = participantCount;
    }

    private void validate(String title, LocalDateTime startsAt, double lat, double lng, UUID creatorId) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Event title must not be empty.");
        }
        if (startsAt == null || startsAt.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Event startsAt must be in the future.");
        }
        if (lat < -90 || lat > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90.");
        }
        if (lng < -180 || lng > 180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180.");
        }
        if (creatorId == null) {
            throw new IllegalArgumentException("Event must have a creator.");
        }
    }

    // --- GETTER ---
    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public LocalDateTime getStartsAt() { return startsAt; }
    public String getPlaceName() { return placeName; }
    public double getLat() { return lat; }
    public double getLng() { return lng; }
    public UUID getCreatorId() { return creatorId; }
    public int getParticipantCount() { return participantCount; }

    // --- SETTER ---
    public void setId(UUID id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setCategory(String category) { this.category = category; }
    public void setDescription(String description) { this.description = description; }
    public void setStartsAt(LocalDateTime startsAt) { this.startsAt = startsAt; }
    public void setPlaceName(String placeName) { this.placeName = placeName; }
    public void setLat(double lat) { this.lat = lat; }
    public void setLng(double lng) { this.lng = lng; }
    public void setCreatorId(UUID creatorId) { this.creatorId = creatorId; }
    public void setParticipantCount(int participantCount) { this.participantCount = participantCount; }
}