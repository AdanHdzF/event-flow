package com.edacourse.api.notification.application.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import com.edacourse.api.notification.domain.model.NotificationWebhook;
import com.edacourse.api.notification.domain.repository.NotificationRepository;

import jakarta.inject.Inject;

public class NotificationService {

	private final NotificationRepository notificationRepository;

	private HttpClient httpClient;
	private final String QUEUE_SERVER_URL = "https://9x1n96dg-8091.usw3.devtunnels.ms/api/subscriptions/channels/";
	private final String WEBHOOK_URL = "https://9x1n96dg-8090.usw3.devtunnels.ms/webhook";

	@Inject
	public NotificationService(NotificationRepository notificationRepository) {
		this.notificationRepository = notificationRepository;
	}

	public void notify(String type, String orderId, String details) {
		System.out.println("[NOTIFY] " + type + " | Orden: " + orderId + " | " + details);
	}

	public void subscribeNotificationWebhook(String channelName) {
		try {

			// Crear subscription a webhook (simulado)
			if (httpClient == null) {
				this.httpClient = HttpClient.newBuilder()
						.connectTimeout(Duration.ofSeconds(5))
						.build();
			}

			String payload = """
					{
						"webhook_url": "%s",
						"description": "Webhook para canal %s"
					}
					""".formatted(WEBHOOK_URL, channelName);

			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(
							QUEUE_SERVER_URL + channelName))
					.header("Content-Type", "application/json")
					// .header("X-PubSub-Signature", signature)
					// .header("X-PubSub-Channel", channelName)
					// .header("X-PubSub-Timestamp", timestamp)
					// .header("X-PubSub-Subscription-Id", sub.getId())
					.timeout(Duration.ofSeconds(15))
					.POST(HttpRequest.BodyPublishers.ofString(payload))
					.build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			int status = response.statusCode();

			if (status >= 200 && status < 300) {
				System.out.println("[NOTIFY] Subscription creada exitosamente para canal: " + channelName + " - HTTP "
						+ status + " - " + response.body());

				String body = response.body();
				java.util.regex.Matcher matcher = java.util.regex.Pattern
						.compile("\"secret\"\\s*:\\s*\"([^\"]+)\"")
						.matcher(body);

				String secret = matcher.find() ? matcher.group(1) : null;
				if (secret == null) {
					throw new IllegalStateException("No se encontró el atributo 'secret' en la respuesta: " + body);
				}
				notificationRepository.save(new NotificationWebhook(channelName, secret));

			} else {
				System.err.println("[NOTIFY] Error al crear subscription para canal: " + channelName + " - HTTP "
						+ status + " - " + response.body());
			}

		} catch (Exception e) {
			System.err.println("[NOTIFY] Error al obtener notificación: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public NotificationWebhook getSubscriptionNotificationWebhook(String channelName) {

		NotificationWebhook notificationWebhook = notificationRepository.findByChannelName(channelName)
				.orElseThrow(
						() -> new IllegalArgumentException("No se encontró un webhook para el canal: " + channelName));

		return notificationWebhook;
	}
}
