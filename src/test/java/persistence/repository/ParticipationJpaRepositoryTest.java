package persistence.repository;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.*;
import persistence.entity.*;
import domain.model.ParticipationStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@QuarkusTest
public class ParticipationJpaRepositoryTest {

    @Inject
    ParticipationJpaRepository participationJpaRepository;

    @Inject
    UserJpaRepository userJpaRepository;

    @Inject
    EventJpaRepository eventJpaRepository;

    @Test
    @TestTransaction
    @DisplayName("Sollte eine Teilnahme speichern und über die Event-ID wiederfinden")
    void testSaveAndFindByEventId() {
        UUID userId = UUID.randomUUID();
        UserJpaEntity user = new UserJpaEntity(userId, "Teilnehmer", "member@test.de");
        userJpaRepository.persist(user);

        UUID eventId = UUID.randomUUID();
        EventJpaEntity event = new EventJpaEntity(
                eventId,
                "Party",
                "Feiern",
                "Große Club-Fete",
                LocalDateTime.now().plusDays(1),
                "Club",
                Double.valueOf(48.0),
                Double.valueOf(9.0),                           
                userId,
                2
        );
        eventJpaRepository.persist(event);

        ParticipationJpaEntity participation = new ParticipationJpaEntity(
                UUID.randomUUID(),
                userId,
                eventId,
                ParticipationStatus.GOING,
                LocalDateTime.now()
        );

        participationJpaRepository.persist(participation);

        List<ParticipationJpaEntity> results = participationJpaRepository.findByEventId(eventId);

        Assertions.assertEquals(1, results.size());
        Assertions.assertEquals(userId, results.get(0).getUserId());
        Assertions.assertEquals(ParticipationStatus.GOING, results.get(0).getStatus());
    }
}
