package domain.service;

import domain.model.Participation;
import domain.model.ParticipationStatus;
import domain.port.EventRepository;
import domain.port.ParticipationRepository;
import domain.port.UserRepository;
import domain.model.Event;
import domain.model.User;
import jakarta.ws.rs.NotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParticipationServiceTest {

    private ParticipationService participationService;
    private ParticipationRepository participationRepository;
    private EventRepository eventRepository;
    private UserRepository userRepository;

    private UUID userId;
    private UUID eventId;

    @BeforeEach
    void setUp() {
        participationRepository = mock(ParticipationRepository.class);
        eventRepository = mock(EventRepository.class);
        userRepository = mock(UserRepository.class);
        participationService = new ParticipationService(participationRepository, eventRepository, userRepository);
        userId = UUID.randomUUID();
        eventId = UUID.randomUUID();
    }

    private Event createValidEvent() {
        return new Event("Test Event", "Fun", "Description",
                LocalDateTime.now().plusDays(1), "Place", 10.0, 10.0, UUID.randomUUID());
    }

    @Test
    void shouldCreateNewParticipation_WhenNotExists() {
        User realUser = new User("TestUser", "test@example.com");
        Event realEvent = createValidEvent();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(realEvent));
        when(userRepository.findById(userId)).thenReturn(Optional.of(realUser));
        when(participationRepository.findByUserIdAndEventId(userId, eventId)).thenReturn(Optional.empty());

        Participation result = participationService.registerUserForEvent(userId, eventId, ParticipationStatus.GOING);

        assertNotNull(result);
        assertEquals(ParticipationStatus.GOING, result.getStatus());
        verify(participationRepository).save(any(Participation.class));
    }

    @Test
    void shouldThrowException_WhenEventNotFound() {
        UUID userId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();

        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(jakarta.ws.rs.NotFoundException.class, () -> {
            participationService.registerUserForEvent(userId, eventId, domain.model.ParticipationStatus.GOING);
        });

        verify(participationRepository, never()).save(any());
    }

    @Test
    void shouldThrowException_WhenUserNotFound() {
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(createValidEvent()));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            participationService.registerUserForEvent(userId, eventId, ParticipationStatus.GOING);
        });
    }
}