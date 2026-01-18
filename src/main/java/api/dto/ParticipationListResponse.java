package api.dto;

import java.util.List;

public class ParticipationListResponse {
    public List<ParticipationResponse> items;
    public int page;
    public int size;
    public long total;

    public ParticipationListResponse() {}

    public ParticipationListResponse(List<ParticipationResponse> items, int page, int size, long total) {
        this.items = items;
        this.page = page;
        this.size = size;
        this.total = total;
    }
}
