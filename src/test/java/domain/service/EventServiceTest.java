package domain.service;

import domain.model.Event;
import domain.port.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class EventServiceTest {

    private EventService eventService;
    private EventRepository eventRepository;

    @BeforeEach
    void setUp() {
        eventRepository = mock(EventRepository.class);
        eventService = new EventService(eventRepository);
    }

    @Test
    void shouldCreateEventSuccessfully() {
        UUID creatorId = UUID.randomUUID();
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);

        Event createdEvent = eventService.createEvent(
                "Java Workshop",
                "Education",
                "Learning Java properly",
                futureDate,
                "Community Hall",
                50.0,
                10.0,
                creatorId
        );

        assertNotNull(createdEvent);
        assertNotNull(createdEvent.getId());
        assertEquals("Java Workshop", createdEvent.getTitle());

        verify(eventRepository).save(createdEvent);
    }
}