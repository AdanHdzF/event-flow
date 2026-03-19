package com.edacourse.pubsub;

import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import com.edacourse.pubsub.channel.config.AppBinder;
import com.edacourse.pubsub.channel.config.ObjectMapperProvider;
import com.edacourse.pubsub.channel.infrastructure.messaging.EventBus;
import com.edacourse.pubsub.channel.infrastructure.messaging.KafkaEventBus;
import com.edacourse.pubsub.channel.infrastructure.persistance.SqlServerChannelRepository;
import com.edacourse.pubsub.channel.infrastructure.persistance.SqlServerMessageRepository;
import com.edacourse.pubsub.channel.infrastructure.persistance.SqlServerSubscriptionRepository;
import com.edacourse.pubsub.channel.repository.ChannelRepository;
import com.edacourse.pubsub.channel.repository.MessageRepository;
import com.edacourse.pubsub.channel.repository.SubscriptionRepository;
import com.edacourse.pubsub.channel.rest.ChannelResource;
import com.edacourse.pubsub.channel.rest.PublishResource;
import com.edacourse.pubsub.channel.rest.SubscriptionResource;
import com.edacourse.pubsub.channel.service.ChannelService;
import com.edacourse.pubsub.channel.service.PublishService;
import com.edacourse.pubsub.channel.service.SubscriptionService;

public class Application {
	private static final String BASE_URI = "http://0.0.0.0:8080/";

	public static void main(String[] args) throws Exception {
		EventBus eventBus = new KafkaEventBus();
		ChannelRepository channelRepository = new SqlServerChannelRepository();
		MessageRepository messageRepository = new SqlServerMessageRepository();
		SubscriptionRepository subscriptionRepository = new SqlServerSubscriptionRepository();

		ChannelService channelService = new ChannelService(channelRepository);
		SubscriptionService subscriptionService = new SubscriptionService(subscriptionRepository, channelRepository);
		PublishService publishService = new PublishService(eventBus, messageRepository, channelRepository);

		ResourceConfig config = new ResourceConfig()
				.register(new AppBinder(channelService, subscriptionService, publishService))
				.register(JacksonFeature.class)
				.register(PublishResource.class)
				.register(ChannelResource.class)
				.register(SubscriptionResource.class)
				.register(ObjectMapperProvider.class);

		HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);

		System.out.println("=== EventFlow Platform iniciada ===");
		System.out.println("REST: " + BASE_URI + "api/channels");

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("Apagando PubSub Server...");
			server.shutdownNow();
			eventBus.close();
		}));

		Thread.currentThread().join();
	}
}