package api.dto;

public class UserRequest {
    public String name;
    public String email;

    public UserRequest() {}

    public UserRequest(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
