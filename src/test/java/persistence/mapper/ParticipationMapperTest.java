package persistence.mapper;

import domain.model.Participation;
import domain.model.ParticipationStatus;
import org.junit.jupiter.api.Test;
import persistence.entity.ParticipationJpaEntity;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ParticipationMapperTest {

    private final ParticipationMapper mapper = new ParticipationMapper();

    @Test
    void shouldMapDomainToEntity() {
        // Given: Ein Participation-Objekt aus der Domain
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        // Nutzt den 5er-Konstruktor (muss public sein!)
        Participation domainParticipation = new Participation(
                id, userId, eventId, ParticipationStatus.GOING, now
        );

        // When: Mapping zur Entity
        ParticipationJpaEntity entity = mapper.toEntity(domainParticipation);

        // Then: Prüfung der Werte
        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals(userId, entity.getUserId());
        assertEquals(eventId, entity.getEventId());
        assertEquals(ParticipationStatus.GOING, entity.getStatus());
        assertEquals(now, entity.getCreatedAt());
    }

    @Test
    void shouldMapEntityToDomain() {
        // Given: Eine Entity aus der Datenbank
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ParticipationJpaEntity entity = new ParticipationJpaEntity(
                id, userId, eventId, ParticipationStatus.INTERESTED, now
        );

        // When: Mapping zurück zur Domain
        Participation domainParticipation = mapper.toDomain(entity);

        // Then: Prüfung der Werte
        assertNotNull(domainParticipation);
        assertEquals(id, domainParticipation.getId());
        assertEquals(userId, domainParticipation.getUserId());
        assertEquals(eventId, domainParticipation.getEventId());
        assertEquals(ParticipationStatus.INTERESTED, domainParticipation.getStatus());
        assertEquals(now, domainParticipation.getCreatedAt());
    }
}
