package domain.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.port.EventRepository;
import domain.model.Event;
import java.util.UUID;
import java.time.LocalDateTime;

class EventServiceTest {

    private EventService eventService;     // Das System, das wir testen (SUT)
    private EventRepository eventRepository; // Der Mock (Fake-Datenbank)

    @BeforeEach
    void setUp() {
        // 1. Mock erstellen
        eventRepository = mock(EventRepository.class);
        // 2. Service mit dem Mock verkabeln
        eventService = new EventService(eventRepository);
    }

    @Test
    void shouldCreateEventSuccessfully() {
        // --- ARRANGE (Vorbereiten) ---
        UUID creatorId = UUID.randomUUID();
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);

        // --- ACT (Handeln) ---
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

        // --- ASSERT (Prüfen) ---
        // 1. Zustand prüfen: Wurde das Objekt richtig gebaut?
        assertNotNull(createdEvent);
        assertNotNull(createdEvent.getId()); // Wurde die ID generiert?
        assertEquals("Java Workshop", createdEvent.getTitle());

        // 2. Verhalten prüfen: Wurde der Port aufgerufen?
        verify(eventRepository).save(createdEvent);
    }
}