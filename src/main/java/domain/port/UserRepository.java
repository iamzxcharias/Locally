package domain.port;

import domain.model.User;
import java.util.*;

public interface UserRepository {

    void save(User user);

    boolean existsByEmail(String email);

    Optional<User> findById(UUID id);
    List<User> findAll();

    void delete(UUID id);

    List<User> search(String q, int page, int size);
    long countSearch(String q);
}