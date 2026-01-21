package domain.service;

import domain.model.User;
import domain.port.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
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

    @Transactional
    public User registerUser(String name, String email) {
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

    public List<User> searchUsers(String q, int page, int size) {
        return userRepository.search(q, page, size);
    }

    public long countSearchUsers(String q) {
        return userRepository.countSearch(q);
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));
    }

    @Transactional
    public User updateUser(UUID id, String newName, String newEmail) {
        User user = getUserById(id);

        user.setName(newName);
        user.setEmail(newEmail);

        userRepository.save(user);
        return user;
    }

    @Transactional
    public void deleteUser(UUID id) {
        userRepository.delete(id);
        log.info("User mit ID {} wurde gelöscht", id);
    }
}