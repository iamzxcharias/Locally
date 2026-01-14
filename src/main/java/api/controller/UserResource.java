package api.controller;

import api.dto.UserRequest;
import api.dto.UserResponse;
import domain.model.User;
import domain.service.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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

        return Response.status(Response.Status.CREATED)
                .entity(mapToResponse(user))
                .build();
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
        User user = userService.getUserById(id);
        return mapToResponse(user);
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

    private UserResponse mapToResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }
}
