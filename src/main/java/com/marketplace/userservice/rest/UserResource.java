package com.marketplace.userservice.rest;

import com.marketplace.userservice.dto.DeductRequest;
import com.marketplace.userservice.dto.LoginRequest;
import com.marketplace.userservice.ejb.SessionTrackerEJB;
import com.marketplace.userservice.ejb.UserServiceEJB;
import com.marketplace.userservice.entity.User;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @EJB
    private UserServiceEJB userServiceEJB;

    @EJB
    private SessionTrackerEJB sessionTracker;

    @POST
    @Path("/register/customer")
    public Response registerCustomer(User user) {
        user.setRole("CUSTOMER");

        if (user.getWalletBalance() == null) {
            user.setWalletBalance(300.0);
        }

        User savedUser = userServiceEJB.register(user);
        return Response.ok(savedUser).build();
    }

    @POST
    @Path("/register/provider")
    public Response registerProvider(User user) {
        user.setRole("PROVIDER");
        user.setWalletBalance(0.0);
        return Response.ok(userServiceEJB.register(user)).build();
    }

    // /api/users/register/admin
    @POST
    @Path("/register/admin")
    public Response registerAdmin(User user) {
        user.setRole("ADMIN");
        user.setWalletBalance(0.0);
        return Response.ok(userServiceEJB.register(user)).build();
    }
    @POST
    @Path("/login")
    public Response login(LoginRequest req) {
        try {
            User user = userServiceEJB.login(req.getUsername(), req.getPassword());
            String token = sessionTracker.createSession(user.getId());
            return Response.ok(Map.of("token", token, "userId", user.getId())).build();
        } catch (Exception e) {
            return Response.status(401).entity(Map.of("error", "Invalid credentials")).build();
        }
    }

    @GET
    @Path("/all")
    public Response getAllUsers() {
        return Response.ok(userServiceEJB.getAllUsers()).build();
    }

    @POST
    @Path("/{id}/wallet/add")
    public Response addFunds(@PathParam("id") Long id, Map<String, Double> body) {
        User u = userServiceEJB.addFunds(id, body.get("amount"));
        return Response.ok(u).build();
    }

    @GET
    @Path("/{id}/wallet")
    public Response getWallet(@PathParam("id") Long id) {
        User u = userServiceEJB.findById(id);
        return Response.ok(Map.of("balance", u.getWalletBalance())).build();
    }

    @POST
    @Path("/internal/deduct")
    public Response deduct(DeductRequest req) {
        boolean ok = userServiceEJB.deductFunds(req.getCustomerId(), req.getAmount());
        return Response.ok(Map.of("success", ok)).build();
    }

    @POST
    @Path("/internal/refund")
    public Response refund(DeductRequest req) {
        userServiceEJB.refundFunds(req.getCustomerId(), req.getAmount());
        return Response.ok(Map.of("success", true)).build();
    }


}