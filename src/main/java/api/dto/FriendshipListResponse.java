package api.dto;

import java.util.List;

public class FriendshipListResponse {
    public List<FriendshipResponse> items;
    public int page;
    public int size;
    public long total;

    public FriendshipListResponse() {}

    public FriendshipListResponse(List<FriendshipResponse> items, int page, int size, long total) {
        this.items = items;
        this.page = page;
        this.size = size;
        this.total = total;
    }
}