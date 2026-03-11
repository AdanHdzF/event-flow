package com.edacourse.api.resource;

import com.edacourse.api.domain.Order;
import com.edacourse.api.dto.CreateOrderRequest;
import com.edacourse.api.dto.OrderResponse;
import com.edacourse.api.service.OrderService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
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

}
