package api.controller;

import api.dto.ParticipationRequest;
import api.dto.ParticipationResponse;
import domain.model.Participation;
import domain.service.ParticipationService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

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
}
