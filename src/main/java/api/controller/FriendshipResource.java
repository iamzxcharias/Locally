package api.controller;

import api.dto.FriendshipRequest;
import api.dto.FriendshipResponse;
import domain.model.Friendship;
import domain.service.FriendshipService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/friendships")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FriendshipResource {

    @Inject
    FriendshipService friendshipService;

    @POST
    public Response requestFriendship(FriendshipRequest request) {
        Friendship friendship = friendshipService.requestFriendship(request.requesterId, request.addresseeId);
        return Response.status(Response.Status.CREATED).entity(mapToResponse(friendship)).build();
    }

    @PATCH
    @Path("/{id}/accept")
    public FriendshipResponse acceptFriendship(@PathParam("id") java.util.UUID id) {
        Friendship friendship = friendshipService.acceptFriendship(id);
        return mapToResponse(friendship);
    }

    private FriendshipResponse mapToResponse(Friendship f) {
        return new FriendshipResponse(f.getId(), f.getRequesterId(), f.getAddresseeId(), f.getStatus(), f.getCreatedAt());
    }
}
