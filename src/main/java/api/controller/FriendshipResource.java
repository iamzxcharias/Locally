package api.controller;

import api.dto.FriendshipRequest;
import api.dto.FriendshipResponse;
import api.dto.FriendshipListResponse;
import domain.model.Friendship;
import domain.service.FriendshipService;
import domain.model.FriendshipStatus;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.CacheControl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

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

    @GET
    @Path("/user/{userId}/search")
    public Response searchMyFriendships(
            @PathParam("userId") UUID userId,
            @QueryParam("status") FriendshipStatus status,
            @QueryParam("friendId") UUID friendId,
            @QueryParam("friendQ") String friendQ,
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("20") @QueryParam("size") int size
    ) {
        if (page < 0) throw new BadRequestException("page must be >= 0");
        if (size <= 0 || size > 50) throw new BadRequestException("size must be between 1 and 50");

        List<FriendshipResponse> items = friendshipService.searchFriendships(userId, status, friendId, friendQ, page, size).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        long total = friendshipService.countSearchFriendships(userId, status, friendId, friendQ);

        FriendshipListResponse body = new FriendshipListResponse(items, page, size, total);

        CacheControl cc = new CacheControl();
        cc.setMaxAge(30);

        return Response.ok(body).cacheControl(cc).build();
    }
}
