package domain.service;

import domain.model.User;
import domain.port.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Mail Address already exists"); //
        }
        // 2. Über den Port persistieren (speichern)
        userRepository.save(user);

        // 3. Den erstellten User zurückgeben
        return user;
    }

}
