package api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class EventResponse {
    public UUID id;
    public String title;
    public String category;
    public String description;
    public LocalDateTime startsAt;
    public String placeName;
    public double lat;
    public double lng;
    public UUID creatorId;

    public EventResponse(UUID id, String title, String category, String description,
                         LocalDateTime startsAt, String placeName, double lat, double lng, UUID creatorId) {
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
}