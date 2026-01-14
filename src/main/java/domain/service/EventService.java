package domain.service;

import domain.model.Event;
import domain.port.EventRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class EventService {

    private final EventRepository eventRepository;

    @Inject
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Transactional
    public Event createEvent(String title, String category, String description,
                             LocalDateTime startsAt, String placeName,
                             Double lat, Double lng, UUID creatorId) {

        Event newEvent = new Event(title, category, description, startsAt, placeName, lat, lng, creatorId);
        eventRepository.save(newEvent);
        return newEvent;
    }


    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(UUID id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found with id: " + id));
    }

    @Transactional
    public Event updateEvent(UUID id, String title, String category, String description,
                             LocalDateTime startsAt, String placeName,
                             Double lat, Double lng) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));

        event.setTitle(title);
        event.setCategory(category);
        event.setDescription(description);
        event.setStartsAt(startsAt);
        event.setPlaceName(placeName);
        event.setLat(lat);
        event.setLng(lng);

        eventRepository.save(event);

        return event;
    }

    @Transactional
    public void deleteEvent(UUID id) {
        eventRepository.delete(id);
    }
}