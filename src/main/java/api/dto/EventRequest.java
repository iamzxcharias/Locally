package api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class EventRequest {
    public String title;
    public String category;
    public String description;
    public LocalDateTime startsAt;
    public String placeName;
    public double lat;
    public double lng;
    public UUID creatorId;
}
