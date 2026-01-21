package domain.model;

import java.util.UUID;
import java.util.regex.Pattern;

public class User {

    private final UUID id;
    private String name;
    private String email;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public User(String name, String email) {
        this(UUID.randomUUID(), name, email);
    }

    public User(UUID id, String name, String email) {
        validate(name, email);
        this.id = id;
        this.name = name;
        this.email = email;
    }

    private void validate(String name, String email) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("User name must not be empty.");
        }
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("User email must be valid.");
        }
    }

    public UUID getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}