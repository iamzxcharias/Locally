package persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "events")

public class EventJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String title;

    private String category;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDateTime startsAt;

    private String placeName;

    private Double lat;

    private Double lng;

    @Column(nullable = false)
    private UUID creatorId;

    // --- Konstruktoren ---

    protected EventJpaEntity() {}

    public EventJpaEntity(UUID id, String title, String category, String description,
                          LocalDateTime startsAt, String placeName, Double lat,
                          Double lng, UUID creatorId) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.startsAt = startsAt;
        this.placeName = placeName;
        this.lat = lat;
        this.lng = lng;
        this.creatorId = creatorId;
    }

    // --- Getter ---
    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public LocalDateTime getStartsAt() { return startsAt; }
    public String getPlaceName() { return placeName; }
    public Double getLat() { return lat; }
    public Double getLng() { return lng; }
    public UUID getCreatorId() { return creatorId; }

    // --- Setter ---
    public void setId(UUID id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setCategory(String category) { this.category = category; }
    public void setDescription(String description) { this.description = description; }
    public void setStartsAt(LocalDateTime startsAt) { this.startsAt = startsAt; }
    public void setPlaceName(String placeName) { this.placeName = placeName; }
    public void setLat(Double lat) { this.lat = lat; }
    public void setLng(Double lng) { this.lng = lng; }
    public void setCreatorId(UUID creatorId) { this.creatorId = creatorId; }
}