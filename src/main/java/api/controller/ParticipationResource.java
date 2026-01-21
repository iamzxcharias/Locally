package api.controller;

import api.dto.*;
import domain.model.*;
import domain.service.ParticipationService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/participations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ParticipationResource {

    @Inject
    ParticipationService participationService;

    @POST
    public Response registerForEvent(ParticipationRequest request) {
        Participation p = participationService.registerUserForEvent(
                request.userId,
                request.eventId,
                request.status
        );
        return Response.status(Response.Status.CREATED).entity(mapToResponse(p)).build();
    }

    @GET
    @Path("/{id}")
    public ParticipationResponse getParticipation(@PathParam("id") UUID id) {
        return mapToResponse(participationService.getParticipationById(id));
    }

    @PATCH
    @Path("/{id}")
    public ParticipationResponse updateStatus(@PathParam("id") UUID id, ParticipationRequest request) {
        return mapToResponse(participationService.updateParticipationStatus(id, request.status));
    }

    @DELETE
    @Path("/{id}")
    public Response cancelParticipation(@PathParam("id") UUID id) {
        participationService.cancelParticipation(id);
        return Response.noContent().build();
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
        validatePaging(page, size);

        List<ParticipationResponse> items = participationService.searchParticipations(userId, eventId, status, page, size).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        long total = participationService.countSearchParticipations(userId, eventId, status);
        ParticipationListResponse body = new ParticipationListResponse(items, page, size, total);

        CacheControl cc = new CacheControl();
        cc.setMaxAge(30);

        return Response.ok(body).cacheControl(cc).build();
    }

    private void validatePaging(int page, int size) {
        if (page < 0) throw new BadRequestException("page must be >= 0");
        if (size <= 0 || size > 50) throw new BadRequestException("size must be between 1 and 50");
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
}
