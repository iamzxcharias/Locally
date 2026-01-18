package persistence.repository;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.entity.EventJpaEntity;
import persistence.entity.UserJpaEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@QuarkusTest
public class EventJpaRepositoryTest {

    @Inject
    EventJpaRepository eventJpaRepository;

    @Inject
    UserJpaRepository userJpaRepository;

    @Test
    @TestTransaction
    @DisplayName("Sollte alle Events eines bestimmten Erstellers finden (1:N Beziehung)")
    void testFindByCreatorId() {
        UUID creatorId = UUID.randomUUID();
        UserJpaEntity creator = new UserJpaEntity(creatorId, "Veranstalter", "orga@test.de");
        userJpaRepository.persist(creator);

        EventJpaEntity event1 = new EventJpaEntity(
                UUID.randomUUID(),
                "Konzert im Park",
                "Live Musik unter Bäumen",
                "Stadtpark",
                LocalDateTime.now().plusDays(1),
                "Musik",
                Double.valueOf(49.7913),
                Double.valueOf(9.9534),
                creatorId,
                2
        );

        EventJpaEntity event2 = new EventJpaEntity(
                UUID.randomUUID(),
                "Flohmarkt",
                "Alles muss raus",
                "Marktplatz",
                LocalDateTime.now().plusDays(2),
                "Shopping",
                Double.valueOf(49.7944),
                Double.valueOf(9.9292),
                creatorId,
                5
        );

        eventJpaRepository.persist(event1);
        eventJpaRepository.persist(event2);

        List<EventJpaEntity> creatorEvents = eventJpaRepository.findByCreatorId(creatorId);

        Assertions.assertEquals(2, creatorEvents.size(), "Es sollten 2 Events gefunden werden.");
        Assertions.assertTrue(creatorEvents.stream().anyMatch(e -> e.getTitle().equals("Konzert im Park")));
    }
}