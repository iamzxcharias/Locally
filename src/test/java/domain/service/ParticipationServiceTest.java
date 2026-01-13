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

    private Event createValidEvent(UUID creatorId) {
        return new Event("Test Event", "Fun", "Description",
                LocalDateTime.now().plusDays(1), "Place", 10.0, 10.0, creatorId);
    }

    @Test
    void shouldCreateNewParticipation_WhenNotExists() {
        UUID userId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        Event realEvent = createValidEvent(UUID.randomUUID());
        User realUser = new User("TestUser", "test@example.com");

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(realEvent));
        when(userRepository.findById(userId)).thenReturn(Optional.of(realUser));
        when(participationRepository.findByUserIdAndEventId(userId, eventId)).thenReturn(Optional.empty());

        Participation result = participationService.registerUserForEvent(userId, eventId, ParticipationStatus.GOING);

        assertNotNull(result);
        assertEquals(ParticipationStatus.GOING, result.getStatus());
        verify(participationRepository).save(result);
    }

    @Test
    void shouldUpdateExistingParticipation_WhenAlreadyExists() {
        UUID userId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        Event realEvent = createValidEvent(UUID.randomUUID());
        User realUser = new User("TestUser", "test@example.com");

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(realEvent));
        when(userRepository.findById(userId)).thenReturn(Optional.of(realUser));

        Participation existingParticipation = new Participation(userId, eventId, ParticipationStatus.INTERESTED);
        when(participationRepository.findByUserIdAndEventId(userId, eventId)).thenReturn(Optional.of(existingParticipation));

        Participation updatedResult = participationService.registerUserForEvent(userId, eventId, ParticipationStatus.GOING);

        assertEquals(ParticipationStatus.GOING, updatedResult.getStatus());
        assertEquals(existingParticipation.getId(), updatedResult.getId());
        verify(participationRepository).save(updatedResult);
    }

    @Test
    void shouldThrowException_WhenEventNotFound() {
        UUID userId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();

        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            participationService.registerUserForEvent(userId, eventId, ParticipationStatus.GOING);
        });

        verify(participationRepository, never()).save(any());
    }
}