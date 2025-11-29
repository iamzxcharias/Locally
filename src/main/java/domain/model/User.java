package domain.model;

import java.util.UUID;
import java.util.regex.Pattern;

public class User {

    private final UUID id;
    private final String name;
    private final String email;

    // Einfache Email-Regex für die Domain-Validierung
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public User(String name, String email) {
        validate(name, email);

        this.id = UUID.randomUUID(); // Domain-seitige ID-Erzeugung
        this.name = name;
        this.email = email;
    }

    private void validate(String name, String email) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("User name must not be empty."); //
        }
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("User email must be valid."); //
        }
    }

    // Getter
    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
}