package domain.service;

import domain.model.Event;
import domain.port.EventRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public class EventService {

    private final EventRepository eventRepository;

    // Dependency Injection über Konstruktor (Best Practice in Hexagonal)
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * UC1: Create Event
     * Goal: Persist a new event entity so that it becomes discoverable.
     */
    public Event createEvent(String title, String category, String description,
                             LocalDateTime startsAt, String placeName,
                             Double lat, Double lng, UUID creatorId) {

        // 1. Entität erstellen (Validierung passiert im Event-Konstruktor)
        Event newEvent = new Event(title, category, description, startsAt, placeName, lat, lng, creatorId);

        // 2. Über den Port persistieren (speichern)
        eventRepository.save(newEvent);

        // 3. Das erstellte Event zurückgeben
        return newEvent;
    }
}