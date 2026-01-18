package persistence.mapper;

import domain.model.Event;
import org.junit.jupiter.api.Test;
import persistence.entity.EventJpaEntity;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EventMapperTest {

    private final EventMapper mapper = new EventMapper();

    @Test
    void shouldMapDomainToEntity() {
        UUID id = UUID.randomUUID();
        UUID creatorId = UUID.randomUUID();
        LocalDateTime startsAt = LocalDateTime.now().plusDays(1);

        Event domainEvent = new Event(
                id, "Tech Conference", "IT", "A great talk about Quarkus",
                startsAt, "Würzburg Hub", 49.79, 9.93, creatorId, 3
        );

        EventJpaEntity entity = mapper.toEntity(domainEvent);

        assertNotNull(entity);
        assertEquals(domainEvent.getId(), entity.getId());
        assertEquals(domainEvent.getTitle(), entity.getTitle());
        assertEquals(domainEvent.getCategory(), entity.getCategory());
        assertEquals(domainEvent.getDescription(), entity.getDescription());
    }

    @Test
    void shouldMapEntityToDomain() {
        UUID id = UUID.randomUUID();
        UUID creatorId = UUID.randomUUID();
        LocalDateTime startsAt = LocalDateTime.now().plusDays(2);

        EventJpaEntity entity = new EventJpaEntity(
                id, "Outdoor Party", "Social", "Music and drinks",
                startsAt, "Main River", 50.11, 8.68, creatorId, 4
        );

        Event domainEvent = mapper.toDomain(entity);

        assertNotNull(domainEvent);
        assertEquals(entity.getId(), domainEvent.getId());
        assertEquals(entity.getTitle(), domainEvent.getTitle());
        assertEquals(entity.getCategory(), domainEvent.getCategory());
        assertEquals(entity.getDescription(), domainEvent.getDescription());
    }
}