package api.controller;

import api.dto.EventRequest;
import api.dto.EventResponse;
import domain.model.Event;
import domain.service.EventService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventResource {

    @Inject
    EventService eventService;

    @POST
    public Response createEvent(EventRequest request) {
        Event event = eventService.createEvent(
                request.title,
                request.category,
                request.description,
                request.startsAt,
                request.placeName,
                request.lat,
                request.lng,
                request.creatorId
        );
        return Response.status(Response.Status.CREATED).entity(mapToResponse(event)).build();
    }

    @GET
    public List<EventResponse> getAllEvents() {
        return eventService.getAllEvents().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public EventResponse getEvent(@PathParam("id") UUID id) {
        Event event = eventService.getEventById(id);
        return mapToResponse(event);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteEvent(@PathParam("id") UUID id) {
        eventService.deleteEvent(id);
        return Response.noContent().build();
    }

    // Hilfsmethode für das Mapping von Domain zu DTO
    private EventResponse mapToResponse(Event event) {
        return new EventResponse(
                event.getId(),
                event.getTitle(),
                event.getCategory(),
                event.getDescription(),
                event.getStartsAt(),
                event.getPlaceName(),
                event.getLat(),
                event.getLng(),
                event.getCreatorId()
        );
    }
}