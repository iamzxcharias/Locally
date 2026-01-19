package api.controller;

import api.dto.EventListResponse;
import api.dto.EventRequest;
import api.dto.EventResponse;
import domain.model.Event;
import domain.service.EventService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.CacheControl;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
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
    public Response createEvent(@Valid EventRequest request) {
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
    public Response getEvents(
            @QueryParam("q") String q,
            @QueryParam("category") String category,
            @QueryParam("from") String from,
            @QueryParam("to") String to,
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("20") @QueryParam("size") int size
    ) {
        if (page < 0) throw new BadRequestException("page must be >= 0");
        if (size <= 0 || size > 50) throw new BadRequestException("size must be between 1 and 50");

        LocalDateTime fromDt = parseDateTime(from);
        LocalDateTime toDt = parseDateTime(to);

        if (fromDt != null && toDt != null && fromDt.isAfter(toDt)) {
            throw new BadRequestException("from must be <= to");
        }

        List<EventResponse> items = eventService.searchEvents(q, category, fromDt, toDt, page, size).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        long total = eventService.countSearchEvents(q, category, fromDt, toDt);

        EventListResponse body = new EventListResponse(items, page, size, total);

        CacheControl cc = new CacheControl();
        cc.setMaxAge(30);

        return Response.ok(body).cacheControl(cc).build();
    }

    @GET
    @Path("/{id}")
    public EventResponse getEvent(@PathParam("id") UUID id) {
        Event event = eventService.getEventById(id);
        return mapToResponse(event);
    }

    @PUT
    @Path("/{id}")
    public Response updateEvent(@PathParam("id") UUID id, EventRequest request) {
        Event updated = eventService.updateEvent(
                id,
                request.title,
                request.category,
                request.description,
                request.startsAt,
                request.placeName,
                request.lat,
                request.lng
        );
        return Response.ok(mapToResponse(updated)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteEvent(@PathParam("id") UUID id) {
        eventService.deleteEvent(id);
        return Response.noContent().build();
    }

    private LocalDateTime parseDateTime(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return LocalDateTime.parse(value);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Invalid date-time format: " + value);
        }
    }

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
                event.getCreatorId(),
                event.getParticipantCount()
        );
    }

    @GET
    @Path("/discover")
    public Response discoverEvents(
        @QueryParam("q") String q,
        @QueryParam("category") String category,
        @QueryParam("from") String from,
        @QueryParam("to") String to,
        @DefaultValue("0") @QueryParam("page") int page,
        @DefaultValue("20") @QueryParam("size") int size
    ) {
        if (page < 0) throw new BadRequestException("page must be >= 0");
        if (size <= 0 || size > 50) throw new BadRequestException("size must be between 1 and 50");

        LocalDateTime fromDt = parseDateTime(from);
        LocalDateTime toDt = parseDateTime(to);

        if (fromDt == null) {
            fromDt = LocalDateTime.now();
        }

        if (fromDt != null && toDt != null && fromDt.isAfter(toDt)) {
        throw new BadRequestException("from must be <= to");
        }

        List<EventResponse> items = eventService.searchEvents(q, category, fromDt, toDt, page, size).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());

        long total = eventService.countSearchEvents(q, category, fromDt, toDt);

        EventListResponse body = new EventListResponse(items, page, size, total);

        CacheControl cc = new CacheControl();
        cc.setMaxAge(30);

        return Response.ok(body).cacheControl(cc).build();
    }

}