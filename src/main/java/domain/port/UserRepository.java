package domain.port;

import domain.model.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    void save(User user);
    boolean existsByEmail(String email);
    Optional<User> findById(UUID id);
    List<User> findAll();

    void delete(UUID id);
}