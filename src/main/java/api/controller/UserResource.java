package api.controller;

import api.dto.*;
import domain.model.*;
import domain.service.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService;

    @POST
    public Response registerUser(UserRequest request) {
        User user = userService.registerUser(request.name, request.email);
        return Response.status(Response.Status.CREATED).entity(mapToResponse(user)).build();
    }

    @GET
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public UserResponse getUser(@PathParam("id") UUID id) {
        return mapToResponse(userService.getUserById(id));
    }

    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") UUID id, UserRequest request) {
        User updated = userService.updateUser(id, request.name, request.email);
        return Response.ok(mapToResponse(updated)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") UUID id) {
        userService.deleteUser(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/search")
    public Response searchUsers(
            @QueryParam("q") String q,
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("20") @QueryParam("size") int size
    ) {
        validatePaging(page, size);

        List<UserResponse> items = userService.searchUsers(q, page, size).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        long total = userService.countSearchUsers(q);
        UserListResponse body = new UserListResponse(items, page, size, total);

        CacheControl cc = new CacheControl();
        cc.setMaxAge(30);

        return Response.ok(body).cacheControl(cc).build();
    }

    private void validatePaging(int page, int size) {
        if (page < 0) throw new BadRequestException("page must be >= 0");
        if (size <= 0 || size > 50) throw new BadRequestException("size must be between 1 and 50");
    }

    private UserResponse mapToResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }
}