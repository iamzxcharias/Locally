package domain.service;

import domain.model.Participation;
import domain.model.ParticipationStatus;
import domain.port.EventRepository;
import domain.port.ParticipationRepository;
import domain.port.UserRepository;
import domain.model.Event;
import domain.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParticipationServiceTest {

    private ParticipationService participationService;

    // Mocks nur für die externen Ports (Datenbank-Zugriff)
    private ParticipationRepository participationRepository;
    private EventRepository eventRepository;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        participationRepository = mock(ParticipationRepository.class);
        eventRepository = mock(EventRepository.class);
        userRepository = mock(UserRepository.class);

        participationService = new ParticipationService(participationRepository, eventRepository, userRepository);
    }

    // Hilfsmethode, um ein valides Event zu erzeugen (vermeidet Code-Duplizierung)
    private Event createValidEvent(UUID creatorId) {
        return new Event(
                "Test Event",
                "Fun",
                "Description",
                LocalDateTime.now().plusDays(1), // Muss in der Zukunft liegen
                "Place",
                10.0,
                10.0,
                creatorId
        );
    }

    @Test
    void shouldCreateNewParticipation_WhenNotExists() {
        // --- ARRANGE ---
        UUID userId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        UUID creatorId = UUID.randomUUID();

        // Statt mock(Event.class) nutzen wir ein echtes Objekt.
        // Das umgeht den Java 25 Byte Buddy Fehler.
        Event realEvent = createValidEvent(creatorId);
        User realUser = new User("TestUser", "test@example.com");

        // Simuliere: Event und User existieren (Repos geben echte Objekte zurück)
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(realEvent));
        when(userRepository.findById(userId)).thenReturn(Optional.of(realUser));

        // Simuliere: Es gibt noch KEINE Teilnahme
        when(participationRepository.findByUserIdAndEventId(userId, eventId)).thenReturn(Optional.empty());

        // --- ACT ---
        Participation result = participationService.updateParticipation(userId, eventId, ParticipationStatus.GOING);

        // --- ASSERT ---
        assertNotNull(result);
        assertEquals(ParticipationStatus.GOING, result.getStatus());
        assertEquals(userId, result.getUserId());
        assertEquals(eventId, result.getEventId());

        verify(participationRepository).save(result);
    }

    @Test
    void shouldUpdateExistingParticipation_WhenAlreadyExists() {
        // --- ARRANGE ---
        UUID userId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        UUID creatorId = UUID.randomUUID();

        Event realEvent = createValidEvent(creatorId);
        User realUser = new User("TestUser", "test@example.com");

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(realEvent));
        when(userRepository.findById(userId)).thenReturn(Optional.of(realUser));

        // Simuliere: Es GIBT schon eine Teilnahme ("INTERESTED")
        Participation existingParticipation = new Participation(userId, eventId, ParticipationStatus.INTERESTED);
        when(participationRepository.findByUserIdAndEventId(userId, eventId)).thenReturn(Optional.of(existingParticipation));

        // --- ACT ---
        Participation updatedResult = participationService.updateParticipation(userId, eventId, ParticipationStatus.GOING);

        // --- ASSERT ---
        assertEquals(ParticipationStatus.GOING, updatedResult.getStatus());
        assertEquals(existingParticipation.getId(), updatedResult.getId()); // ID muss gleich bleiben

        verify(participationRepository).save(updatedResult);
    }

    @Test
    void shouldThrowException_WhenEventNotFound() {
        // --- ARRANGE ---
        UUID userId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();

        // Simuliere: Event NICHT gefunden
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        // --- ACT & ASSERT ---
        assertThrows(IllegalArgumentException.class, () -> {
            participationService.updateParticipation(userId, eventId, ParticipationStatus.GOING);
        });

        verify(participationRepository, never()).save(any());
    }
}