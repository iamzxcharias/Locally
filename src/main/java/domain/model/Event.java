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

    public Event(String title, String category, String description,
                 LocalDateTime startsAt, String placeName, double lat,
                 double lng, UUID creatorId) {
        this(UUID.randomUUID(), title, category, description, startsAt, placeName, lat, lng, creatorId, 0);
    }

    public Event(UUID id, String title, String category, String description,
                 LocalDateTime startsAt, String placeName, double lat,
                 double lng, UUID creatorId, int participantCount) {
        validate(title, startsAt, lat, lng, creatorId);
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
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Event title must not be empty.");
        if (startsAt == null || startsAt.isBefore(LocalDateTime.now())) throw new IllegalArgumentException("Event startsAt must be in the future.");
        if (lat < -90 || lat > 90) throw new IllegalArgumentException("Latitude must be between -90 and 90.");
        if (lng < -180 || lng > 180) throw new IllegalArgumentException("Longitude must be between -180 and 180.");
        if (creatorId == null) throw new IllegalArgumentException("Event must have a creator.");
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getStartsAt() { return startsAt; }
    public void setStartsAt(LocalDateTime startsAt) { this.startsAt = startsAt; }

    public String getPlaceName() { return placeName; }
    public void setPlaceName(String placeName) { this.placeName = placeName; }

    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }

    public double getLng() { return lng; }
    public void setLng(double lng) { this.lng = lng; }

    public UUID getCreatorId() { return creatorId; }
    public void setCreatorId(UUID creatorId) { this.creatorId = creatorId; }

    public int getParticipantCount() { return participantCount; }
    public void setParticipantCount(int participantCount) { this.participantCount = participantCount; }
}