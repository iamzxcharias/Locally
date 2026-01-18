package api.dto;

import java.util.List;

public class UserListResponse {
    public List<UserResponse> items;
    public int page;
    public int size;
    public long total;

    public UserListResponse() {}

    public UserListResponse(List<UserResponse> items, int page, int size, long total) {
        this.items = items;
        this.page = page;
        this.size = size;
        this.total = total;
    }
}