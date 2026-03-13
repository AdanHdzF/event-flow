package com.edacourse.api;

import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import com.edacourse.api.config.AppBinder;
import com.edacourse.api.config.ObjectMapperProvider;
import com.edacourse.api.infrastructure.messaging.EventBus;
import com.edacourse.api.infrastructure.messaging.EventSerializer;
import com.edacourse.api.infrastructure.messaging.JsonEventSerializer;
import com.edacourse.api.infrastructure.messaging.KafkaEventBus;
import com.edacourse.api.resource.OrderResource;
import com.edacourse.api.resource.OrderSseResource;
import com.edacourse.api.service.InventoryService;
import com.edacourse.api.service.NotificationService;
import com.edacourse.api.service.PaymentService;
import com.edacourse.api.subscriber.InventorySubscriber;
import com.edacourse.api.subscriber.NotificationSubscriber;
import com.edacourse.api.subscriber.PaymentSubscriber;
import com.edacourse.api.subscriber.SseBridgeSubscriber;

// import com.edacourse.api.di.Container;
// import com.edacourse.api.infrastructure.messaging.EventBus;
// import com.edacourse.api.infrastructure.messaging.EventSerializer;
// import com.edacourse.api.infrastructure.messaging.InMemoryEventBus;
// import com.edacourse.api.infrastructure.messaging.JsonEventSerializer;
// import com.edacourse.api.infrastructure.messaging.OrderEvent;
// import com.edacourse.api.infrastructure.notification.ConsoleNotification;
// import com.edacourse.api.infrastructure.notification.NotificationService;
// import com.edacourse.api.service.OrderService;

public class Application {

	private static final String BASE_URI = "http://0.0.0.0:8080/";

	public static void main(String[] args) throws InterruptedException {
		System.out.println("Application started at " + BASE_URI);

		EventSerializer serializer = new JsonEventSerializer();
		EventBus eventBus = new KafkaEventBus(serializer);
		OrderSseResource orderSseResource = new OrderSseResource();
		InventoryService inventoryService = new InventoryService(eventBus);
		PaymentService paymentService = new PaymentService(eventBus);
		NotificationService notificationService = new NotificationService();

		ResourceConfig config = new ResourceConfig()
				.register(new AppBinder(serializer, eventBus,
						orderSseResource))
				.register(JacksonFeature.class)
				.register(ObjectMapperProvider.class)
				.register(OrderResource.class)
				.register(orderSseResource);

		new InventorySubscriber(eventBus, inventoryService);
		new PaymentSubscriber(eventBus, paymentService);
		new NotificationSubscriber(eventBus, notificationService);
		new SseBridgeSubscriber(eventBus, serializer, orderSseResource);

		HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("Shutting down server...");
			server.shutdownNow();
			eventBus.close();
		}));

		Thread.currentThread().join();
	}

	/*
	 * public static void main(String[] args) {
	 * try {
	 * Container container = new Container();
	 * container.register(NotificationService.class, ConsoleNotification.class);
	 * container.register(PromotionService.class,
	 * TenPercentDiscountPromotion.class);
	 * container.register(OrderService.class, OrderService.class);
	 * // container.register(EventBus.class, RabbitMQEventBusEventBus.class);
	 * // container.register(EventBus.class, KafkaEventBus.class);
	 * container.register(EventBus.class, InMemoryEventBus.class);
	 * container.register(EventSerializer.class, JsonEventSerializer.class);
	 * 
	 * Thread.sleep(20000);
	 * 
	 * EventBus eventBus = container.resolve(EventBus.class);
	 * eventBus.subscribe("order.created", OrderEvent.class, event -> {
	 * System.out.println("Evento recibido: " + event);
	 * });
	 * 
	 * OrderService orderService = container.resolve(OrderService.class);
	 * orderService.createOrder("Laptop", 10.99);
	 * orderService.createOrder("Smartphone", 20.49);
	 * orderService.createOrder("Tablet", 15.75);
	 * 
	 * Thread.sleep(10000);
	 * 
	 * eventBus.close();
	 * } catch (Exception e) {
	 * e.printStackTrace();
	 * System.out.println("Fallo");
	 * }
	 * 
	 * }
	 */
}