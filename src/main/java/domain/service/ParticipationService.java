package domain.service;

import domain.model.*;
import domain.port.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ParticipationService {

    private final ParticipationRepository participationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    @Inject
    public ParticipationService(ParticipationRepository participationRepository,
                                EventRepository eventRepository,
                                UserRepository userRepository,
                                FriendshipRepository friendshipRepository) {
        this.participationRepository = participationRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
    }

    @Transactional
    public Participation registerUserForEvent(UUID userId, UUID eventId, ParticipationStatus status) {
        validateEntitiesExist(userId, eventId);

        Participation participationToSave = participationRepository.findByUserIdAndEventId(userId, eventId)
                .map(p -> p.withStatus(status))
                .orElseGet(() -> new Participation(userId, eventId, status));

        participationRepository.save(participationToSave);
        updateEventParticipantCount(eventId);

        return participationToSave;
    }

    public Participation getParticipationById(UUID id) {
        return participationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Participation not found with id: " + id));
    }

    @Transactional
    public Participation updateParticipationStatus(UUID id, ParticipationStatus newStatus) {
        Participation participation = getParticipationById(id);
        participation.setStatus(newStatus);
        participationRepository.save(participation);
        return participation;
    }

    @Transactional
    public void cancelParticipation(UUID id) {
        Participation participation = getParticipationById(id);
        UUID eventId = participation.getEventId();

        participationRepository.delete(id);
        updateEventParticipantCount(eventId);
    }

    public List<Participation> searchParticipations(UUID userId, UUID eventId, ParticipationStatus status, int page, int size) {
        return participationRepository.search(userId, eventId, status, page, size);
    }

    public long countSearchParticipations(UUID userId, UUID eventId, ParticipationStatus status) {
        return participationRepository.countSearch(userId, eventId, status);
    }

    private void validateEntitiesExist(UUID userId, UUID eventId) {
        if (eventRepository.findById(eventId).isEmpty()) {
            throw new NotFoundException("Event not found with id: " + eventId);
        }
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("User not found with id: " + userId);
        }
    }

    private void updateEventParticipantCount(UUID eventId) {
        int newCount = (int) participationRepository.countByEventId(eventId);
        eventRepository.updateParticipantCount(eventId, newCount);
    }
    @Transactional
    public void inviteFriend(UUID currentUserId, UUID friendId, UUID eventId) {
        // Check 1: Sind sie Freunde?
        if (!friendshipRepository.areFriends(currentUserId, friendId)) {
            throw new IllegalArgumentException("Du kannst nur bestätigte Freunde einladen.");
        }

        // Check 2: Existiert das Event?
        if (eventRepository.findById(eventId).isEmpty()) {
            throw new NotFoundException("Event nicht gefunden.");
        }

        // Check 3: Hat der User schon einen Status? (Teilnahme oder schon eingeladen)
        Optional<Participation> existing = participationRepository.findByUserIdAndEventId(friendId, eventId);
        if (existing.isPresent()) {
            throw new IllegalStateException("User hat bereits einen Status für dieses Event.");
        }

        // Alles ok -> Einladung erstellen
        Participation invitation = new Participation(
                UUID.randomUUID(),
                friendId,
                eventId,
                ParticipationStatus.INVITED,
                LocalDateTime.now()
        );

        participationRepository.save(invitation);
    }
}