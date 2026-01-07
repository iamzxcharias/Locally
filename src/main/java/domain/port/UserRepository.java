package domain.port;

import domain.model.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {


    //Persistiert einen neuen oder aktualisierten User.
    void save(User user);


    //Sucht einen User anhand seiner eindeutigen ID.
    Optional<User> findById(UUID id);


    //Prüft, ob die E-Mail bereits vergeben ist.
    //Wichtig für die Validierung bei der Registrierung (Invariante: Unique Email).
    boolean existsByEmail(String email);
}