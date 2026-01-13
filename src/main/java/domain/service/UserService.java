package domain.service;

import domain.model.User;
import domain.port.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional; // Neu für Datenbank-Schreibrechte
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    @Inject
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Registriert einen neuen User
     * @Transactional stellt sicher, dass der User sicher in der DB landet.
     */
    @Transactional
    public User registerUser(String name, String email) {
        // Keine doppelten E-Mails
        if (userRepository.existsByEmail(email)) {
            log.warn("Registrierung fehlgeschlagen: Email {} existiert bereits", email);
            throw new IllegalArgumentException("Mail Address already exists");
        }

        User user = new User(name, email);

        userRepository.save(user);

        log.info("User erfolgreich registriert: {} mit ID {}", name, user.getId());
        return user;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Transactional
    public void deleteUser(UUID id) {
        userRepository.delete(id);
        log.info("User mit ID {} wurde gelöscht", id);
    }
}
