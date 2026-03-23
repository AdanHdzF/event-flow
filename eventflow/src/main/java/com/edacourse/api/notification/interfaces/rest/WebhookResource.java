package com.edacourse.api.notification.interfaces.rest;

import com.edacourse.api.notification.application.service.NotificationService;
import com.edacourse.api.notification.domain.model.NotificationWebhook;
import com.edacourse.api.security.HmacSigner;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/webhook")
public class WebhookResource {
	private final NotificationService notificationService;

	@Inject
	private HmacSigner hmacSigner;

	@Inject
	public WebhookResource(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response handleWebhook(
			@HeaderParam("X-PubSub-Signature") String sig,
			@HeaderParam("X-PubSub-Channel") String channel,
			String body) {

		NotificationWebhook webhook = notificationService.getSubscriptionNotificationWebhook(channel);

		// String secret = System.getenv("WEBHOOK_SECRET");
		boolean isValid = hmacSigner.verify(body, webhook.getSecret(), sig);
		if (!isValid) {
			return Response.status(401).build();
		}
		System.out.println("[*** RECEIVER - EVENT-FLOW ***] Canal=" + channel
				+ " Payload=" + body);
		return Response.ok().build();
	}
}
