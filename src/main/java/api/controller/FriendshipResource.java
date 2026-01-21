package api.controller;

import api.dto.*;
import domain.model.*;
import domain.service.FriendshipService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.List;
import java.util.UUID;
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
    public List<FriendshipResponse> getMyFriendships(@PathParam("userId") UUID userId) {
        return friendshipService.getFriendshipsForUser(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @PATCH
    @Path("/{id}/accept")
    public FriendshipResponse acceptFriendship(@PathParam("id") UUID id) {
        return mapToResponse(friendshipService.acceptFriendship(id));
    }

    @DELETE
    @Path("/{id}")
    public Response cancelOrDeleteFriendship(@PathParam("id") UUID id) {
        friendshipService.deleteFriendship(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/user/{userId}/search")
    public Response searchMyFriendships(
            @PathParam("userId") UUID userId,
            @QueryParam("status") String status,
            @QueryParam("friendId") UUID friendId,
            @QueryParam("friendQ") String friendQ,
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("20") @QueryParam("size") int size
    ) {
        validatePaging(page, size);
        FriendshipStatus statusEnum = parseStatus(status);

        List<FriendshipResponse> items = friendshipService.searchFriendships(userId, statusEnum, friendId, friendQ, page, size).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        long total = friendshipService.countSearchFriendships(userId, statusEnum, friendId, friendQ);
        FriendshipListResponse body = new FriendshipListResponse(items, page, size, total);

        CacheControl cc = new CacheControl();
        cc.setMaxAge(30);

        return Response.ok(body).cacheControl(cc).build();
    }

    private FriendshipStatus parseStatus(String status) {
        if (status == null || status.isBlank()) return null;
        try {
            return FriendshipStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid status: " + status);
        }
    }

    private void validatePaging(int page, int size) {
        if (page < 0) throw new BadRequestException("page must be >= 0");
        if (size <= 0 || size > 50) throw new BadRequestException("size must be between 1 and 50");
    }

    private FriendshipResponse mapToResponse(Friendship f) {
        return new FriendshipResponse(
                f.getId(),
                f.getRequesterId(),
                f.getAddresseeId(),
                f.getStatus(),
                f.getCreatedAt()
        );
    }
}