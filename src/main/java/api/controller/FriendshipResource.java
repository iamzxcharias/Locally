package api.controller;

import api.dto.FriendshipRequest;
import api.dto.FriendshipResponse;
import domain.model.Friendship;
import domain.service.FriendshipService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

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

    @GET
    @Path("/user/{userId}")
    public List<FriendshipResponse> getMyFriendships(@PathParam("userId") java.util.UUID userId) {
        return friendshipService.getFriendshipsForUser(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
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

    @DELETE
    @Path("/{id}")
    public Response cancelOrDeleteFriendship(@PathParam("id") java.util.UUID id) {
        friendshipService.deleteFriendship(id);
        return Response.noContent().build();
    }
}
