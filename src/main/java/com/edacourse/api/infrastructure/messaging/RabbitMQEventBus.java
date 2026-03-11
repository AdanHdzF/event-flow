package com.edacourse.api.infrastructure.messaging;

import java.nio.charset.StandardCharsets;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQEventBus implements EventBus {

	private final Connection connection;
	private final Channel channel;
	private final EventSerializer serializer;

	public RabbitMQEventBus(EventSerializer serializer) {
		this.serializer = serializer;

		String host = System.getenv("RABBITMQ_HOST");
		int port = Integer.parseInt(System.getenv("RABBITMQ_PORT"));
		String username = System.getenv("RABBITMQ_USER");
		String password = System.getenv("RABBITMQ_PASSWORD");

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(host);
		factory.setPort(port);
		factory.setUsername(username);
		factory.setPassword(password);

		Connection con = null;
		Channel ch = null;

		for (int i = 0; i < 10; i++) {
			try {
				con = factory.newConnection();
				ch = con.createChannel();

				System.out.println("Connected to RabbitMQ on " + host + ":" + port);
				break;
			} catch (Exception e) {
				System.err.println("Error connecting to RabbitMQ (attempt " + (i + 1) + "): " + e.getMessage());
				try {
					Thread.sleep(2000);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
				}
			}
		}

		this.connection = con;
		this.channel = ch;

	}

	@Override
	public void publish(String topic, Object event) {
		try {
			channel.exchangeDeclare(topic, "fanout", true);
			String message = serializer.serialize(event);
			channel.basicPublish(topic, "", null, message.getBytes());
		} catch (Exception e) {
			throw new RuntimeException("Error publishing event to RabbitMQ: " + e.getMessage(), e);
		}
	}

	@Override
	public <T> void subscribe(String topic, Class<T> eventType, EventHandler<T> handler) {
		try {
			channel.exchangeDeclare(topic, "fanout", true);
			String queue = channel.queueDeclare().getQueue();
			channel.queueBind(queue, topic, "");

			channel.basicConsume(queue, true, (consumerTag, delivery) -> {
				String json = new String(delivery.getBody(), StandardCharsets.UTF_8);
				T event = serializer.deserialize(json, eventType);
				handler.handle(event);
			}, consumerTag -> {
			});
		} catch (Exception e) {
			throw new RuntimeException("Error subscribing to RabbitMQ: " + e.getMessage(), e);
		}
	}

	@Override
	public void close() {
		try {
			if (channel != null && channel.isOpen())
				channel.close();
			if (connection != null && connection.isOpen())
				connection.close();
		} catch (Exception e) {
			throw new RuntimeException("Error closing RabbitMQ connection: " + e.getMessage(), e);
		}
	}

}
