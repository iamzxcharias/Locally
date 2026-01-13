package api.dto;

import java.util.UUID;

public class UserResponse {
    public UUID id;
    public String name;
    public String email;

    public UserResponse(UUID id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}