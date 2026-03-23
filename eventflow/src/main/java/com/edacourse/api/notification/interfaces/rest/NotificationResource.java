package com.edacourse.api.notification.interfaces.rest;

import com.edacourse.api.notification.application.service.NotificationService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/notifications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotificationResource {
	private final NotificationService notificationService;

	@Inject
	public NotificationResource(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	@GET
	@Path("/{channelName}")
	public Response getNotification(@PathParam("channelName") String channelName) {

		notificationService.subscribeNotificationWebhook(channelName);

		return Response.ok().build();
	}
}
