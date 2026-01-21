package api.controller;

import api.dto.ParticipationRequest;
import api.dto.ParticipationResponse;
import api.dto.ParticipationListResponse;
import domain.model.Participation;
import domain.service.ParticipationService;
import domain.model.ParticipationStatus;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.CacheControl;
import api.dto.InvitationRequest;

import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

@Path("/participations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ParticipationResource {

    @Inject
    ParticipationService participationService;

    @POST
    public Response registerForEvent(ParticipationRequest request) {
        Participation participation = participationService.registerUserForEvent(
                request.userId,
                request.eventId,
                request.status
        );

        return Response.status(Response.Status.CREATED)
                .entity(mapToResponse(participation))
                .build();
    }

    @GET
    @Path("/{id}")
    public ParticipationResponse getParticipation(@PathParam("id") UUID id) {
        Participation p = participationService.getParticipationById(id);
        return mapToResponse(p);
    }

    @PATCH
    @Path("/{id}")
    public ParticipationResponse updateStatus(@PathParam("id") UUID id, ParticipationRequest request) {
        // Jackson wandelt das JSON-Feld "status" automatisch in das Enum um
        Participation updated = participationService.updateParticipationStatus(id, request.status);
        return mapToResponse(updated);
    }

    @DELETE
    @Path("/{id}")
    public Response cancelParticipation(@PathParam("id") UUID id) {
        participationService.cancelParticipation(id);
        return Response.noContent().build();
    }

    private ParticipationResponse mapToResponse(Participation p) {
        return new ParticipationResponse(
                p.getId(),
                p.getUserId(),
                p.getEventId(),
                p.getStatus(),
                p.getCreatedAt()
        );
    }

    @GET
    @Path("/search")
    public Response searchParticipations(
            @QueryParam("userId") UUID userId,
            @QueryParam("eventId") UUID eventId,
            @QueryParam("status") ParticipationStatus status,
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("20") @QueryParam("size") int size
    ) {
        if (page < 0) throw new BadRequestException("page must be >= 0");
        if (size <= 0 || size > 50) throw new BadRequestException("size must be between 1 and 50");

        List<ParticipationResponse> items = participationService.searchParticipations(userId, eventId, status, page, size).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        long total = participationService.countSearchParticipations(userId, eventId, status);

        ParticipationListResponse body = new ParticipationListResponse(items, page, size, total);

        CacheControl cc = new CacheControl();
        cc.setMaxAge(30);

        return Response.ok(body).cacheControl(cc).build();
    }
    @POST
    @Path("/invite")
    public Response inviteUser(InvitationRequest request) {
        // TODO: Im echten Betrieb ID aus SecurityContext (JWT) holen.
        // Für die Aufgabe nutzen wir Alice aus den Testdaten:
        UUID currentUserId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        try {
            participationService.inviteFriend(currentUserId, request.targetUserId, request.eventId);
            return Response.status(Response.Status.CREATED).build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            // 400 Bad Request bei Logikfehlern (kein Freund / schon dabei)
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (NotFoundException e) {
            // 404 Not Found wenn Event nicht existiert
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
}
