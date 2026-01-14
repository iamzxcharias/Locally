package domain.service;

import domain.model.Participation;
import domain.model.ParticipationStatus;
import domain.port.EventRepository;
import domain.port.ParticipationRepository;
import domain.port.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ParticipationService {

    private final ParticipationRepository participationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Inject
    public ParticipationService(ParticipationRepository participationRepository,
                                EventRepository eventRepository,
                                UserRepository userRepository) {
        this.participationRepository = participationRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Participation registerUserForEvent(UUID userId, UUID eventId, ParticipationStatus status) {
        if (eventRepository.findById(eventId).isEmpty()) {
            throw new NotFoundException("Event not found with id: " + eventId);
        }
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("User not found with id: " + userId);
        }

        Optional<Participation> existingParticipation = participationRepository.findByUserIdAndEventId(userId, eventId);

        Participation participationToSave;

        if (existingParticipation.isPresent()) {
            participationToSave = existingParticipation.get().withStatus(status);
        } else {
            participationToSave = new Participation(
                    UUID.randomUUID(),
                    userId,
                    eventId,
                    status,
                    LocalDateTime.now()
            );
        }

        participationRepository.save(participationToSave);
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
        if (participationRepository.findById(id).isEmpty()) {
            throw new NotFoundException("Participation not found with id: " + id);
        }
        participationRepository.delete(id);
    }
}