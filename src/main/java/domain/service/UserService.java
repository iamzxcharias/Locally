package domain.service;

import domain.model.User;
import domain.port.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

@ApplicationScoped // Wichtig für Quarkus
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    @Inject // Erlaubt Quarkus, das UserRepository einzusetzen
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Registriert einen neuen User (Deine bestehende Logik)
     */
    public User registerUser(String name, String email) {
        // Wir prüfen die E-Mail, bevor wir das Objekt überhaupt erstellen
        if (userRepository.existsByEmail(email)) {
            log.warn("Registrierung fehlgeschlagen: Email {} existiert bereits", email);
            throw new IllegalArgumentException("Mail Address already exists");
        }

        User user = new User(name, email);
        userRepository.save(user);
        log.info("User registriert: {} ({})", name, user.getId());
        return user;
    }

    // --- Neue Methoden für die REST-API (Issue #11) ---

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public void deleteUser(UUID id) {
        userRepository.delete(id);
        log.info("User gelöscht: {}", id);
    }
}
