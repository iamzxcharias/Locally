package persistence.repository;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.entity.UserJpaEntity;

import java.util.Optional;
import java.util.UUID;

@QuarkusTest
public class UserJpaRepositoryTest {

    @Inject
    UserJpaRepository userJpaRepository;

    @Test
    @TestTransaction
    @DisplayName("Sollte einen User speichern und über die Email wiederfinden")
    void testSaveAndFindByEmail() {
        UUID userId = UUID.randomUUID();
        String email = "test@locally.de";
        UserJpaEntity user = new UserJpaEntity(userId, "Max Mustermann", email);

        userJpaRepository.persist(user);

        Optional<UserJpaEntity> foundUser = userJpaRepository.findByEmail(email);

        Assertions.assertTrue(foundUser.isPresent(), "User sollte in der Datenbank gefunden werden");
        Assertions.assertEquals("Max Mustermann", foundUser.get().getName());
        Assertions.assertEquals(userId, foundUser.get().getId());
    }

    @Test
    @TestTransaction
    @DisplayName("Sollte leer zurückgeben, wenn Email nicht existiert")
    void testFindByEmailNotFound() {
        Optional<UserJpaEntity> foundUser = userJpaRepository.findByEmail("nicht-existent@locally.de");
        Assertions.assertTrue(foundUser.isEmpty());
    }
}
