package domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Event {

    private final UUID id;
    private final String title;
    private final String category;
    private final String description;
    private final LocalDateTime startsAt;
    private final String placeName;
    private final Double lat;
    private final Double lng;
    private final UUID creatorId; // referenziert User

    // Konstruktor
    public Event(String title, String category, String description, LocalDateTime startsAt,
                 String placeName, Double lat, Double lng, UUID creatorId) {

        // 1. Invarianten prüfen (Validierung VOR Objekterzeugung)
        validate(title, startsAt, lat, lng, creatorId);

        // 2. ID wird domainseitig erzeugt
        this.id = UUID.randomUUID();

        this.title = title;
        this.category = category;
        this.description = description;
        this.startsAt = startsAt;
        this.placeName = placeName;
        this.lat = lat;
        this.lng = lng;
        this.creatorId = creatorId;
    }

    // Validierungs-Logik (Strikte Domain-Regeln)
    private void validate(String title, LocalDateTime startsAt, Double lat, Double lng, UUID creatorId) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Event title must not be empty."); //
        }
        if (startsAt == null || startsAt.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Event start time must be in the future."); //
        }
        if (creatorId == null) {
            throw new IllegalArgumentException("Event must have a creator.");
        }
        // Einfache Geo-Validierung (-90 bis +90 Lat, -180 bis +180 Lng)
        if (lat != null && (lat < -90 || lat > 90)) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90."); //
        }
        if (lng != null && (lng < -180 || lng > 180)) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180."); //
        }
    }

    // Getter (Keine Setter, um Immutability zu fördern - das Objekt bleibt stabil)
    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public LocalDateTime getStartsAt() { return startsAt; }
    public String getPlaceName() { return placeName; }
    public Double getLat() { return lat; }
    public Double getLng() { return lng; }
    public UUID getCreatorId() { return creatorId; }
}