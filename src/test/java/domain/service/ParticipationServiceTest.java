package domain.service;

import domain.model.*;
import domain.port.*;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.port.FriendshipRepository;

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
    private FriendshipRepository friendshipRepository;

    private UUID userId;
    private UUID eventId;

    @BeforeEach
    void setUp() {
        participationRepository = mock(ParticipationRepository.class);
        eventRepository = mock(EventRepository.class);
        userRepository = mock(UserRepository.class);
        friendshipRepository = mock(FriendshipRepository.class);
        participationService = new ParticipationService(participationRepository, eventRepository, userRepository,friendshipRepository);
        userId = UUID.randomUUID();
        eventId = UUID.randomUUID();
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
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                participationService.registerUserForEvent(userId, eventId, ParticipationStatus.GOING)
        );

        verify(participationRepository, never()).save(any());
    }

    @Test
    void shouldThrowException_WhenUserNotFound() {
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(createValidEvent()));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                participationService.registerUserForEvent(userId, eventId, ParticipationStatus.GOING)
        );
    }

    private Event createValidEvent() {
        return new Event("Test Event", "Fun", "Description",
                LocalDateTime.now().plusDays(1), "Place", 10.0, 10.0, UUID.randomUUID());
    }
    @Test
    void shouldInviteFriend_WhenAreFriendsAndNotParticipating() {
        // Arrange
        UUID friendId = UUID.randomUUID();
        Event realEvent = createValidEvent();

        // Mocks einrichten
        when(friendshipRepository.areFriends(userId, friendId)).thenReturn(true); // Sind Freunde
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(realEvent)); // Event existiert
        when(participationRepository.findByUserIdAndEventId(friendId, eventId)).thenReturn(Optional.empty()); // Freund nimmt noch nicht teil

        // Act
        participationService.inviteFriend(userId, friendId, eventId);

        // Assert
        // Prüfen, ob save aufgerufen wurde und ob der Status INVITED ist
        verify(participationRepository).save(argThat(p ->
                p.getUserId().equals(friendId) &&
                        p.getEventId().equals(eventId) &&
                        p.getStatus() == ParticipationStatus.INVITED
        ));
    }

    @Test
    void shouldThrowException_WhenInvitingNonFriend() {
        UUID strangerId = UUID.randomUUID();

        // Mock: Sind KEINE Freunde
        when(friendshipRepository.areFriends(userId, strangerId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () ->
                participationService.inviteFriend(userId, strangerId, eventId)
        );

        // Sicherstellen, dass NICHTS gespeichert wurde
        verify(participationRepository, never()).save(any());
    }

    @Test
    void shouldThrowException_WhenFriendAlreadyParticipating() {
        UUID friendId = UUID.randomUUID();
        Event realEvent = createValidEvent();

        when(friendshipRepository.areFriends(userId, friendId)).thenReturn(true);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(realEvent));

        // Mock: Freund ist schon dabei (egal welcher Status)
        Participation existing = new Participation(UUID.randomUUID(), friendId, eventId, ParticipationStatus.GOING, LocalDateTime.now());
        when(participationRepository.findByUserIdAndEventId(friendId, eventId)).thenReturn(Optional.of(existing));

        assertThrows(IllegalStateException.class, () ->
                participationService.inviteFriend(userId, friendId, eventId)
        );
    }
}