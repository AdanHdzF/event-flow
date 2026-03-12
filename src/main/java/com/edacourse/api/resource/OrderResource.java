package com.edacourse.api.resource;

import com.edacourse.api.domain.Order;
import com.edacourse.api.dto.CancelOrderRequest;
import com.edacourse.api.dto.CreateOrderRequest;
import com.edacourse.api.dto.OrderResponse;
import com.edacourse.api.service.OrderService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

	private final OrderService orderService;

	@Inject
	public OrderResource(OrderService orderService) {
		this.orderService = orderService;
	}

	@POST
	public Response createOrder(CreateOrderRequest request) {
		Order order = orderService.createOrder(request);
		return Response.status(Response.Status.CREATED).entity(OrderResponse.from(order)).build();
	}

	@PUT
	@Path("/{id}/cancel")
	public Response cancelOrder(@PathParam("id") String id, CancelOrderRequest request) {
		Order order = orderService.cancelOrder(id, request.getReason());
		return Response.ok(OrderResponse.from(order)).build();
	}

	@GET
	public Response findAllOrders() {
		return Response.ok(orderService.findAllOrders().stream().map(OrderResponse::from).toList()).build();
	}

	@GET
	@Path("/{id}")
	public Response findOrderById(@PathParam("id") String id) {
		Order order = orderService.findOrderById(id);
		if (order != null) {
			return Response.ok(OrderResponse.from(order)).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

}
