package api.dto;

import java.util.List;

public class EventListResponse {
    public List<EventResponse> items;
    public int page;
    public int size;
    public long total;

    public EventListResponse() {}

    public EventListResponse(List<EventResponse> items, int page, int size, long total) {
        this.items = items;
        this.page = page;
        this.size = size;
        this.total = total;
    }
}