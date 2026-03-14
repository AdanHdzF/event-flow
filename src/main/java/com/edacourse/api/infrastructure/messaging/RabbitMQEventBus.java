package com.edacourse.api.infrastructure.messaging;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQEventBus implements EventBus, AdvancedEventBus {

	private final Connection connection;
	private final Channel channel;
	private final EventSerializer serializer;

	public RabbitMQEventBus(EventSerializer serializer) {
		this.serializer = serializer;

		String host = System.getenv("RABBITMQ_HOST");
		int port = Integer.parseInt(System.getenv("RABBITMQ_PORT"));
		String username = System.getenv("RABBITMQ_USERNAME");
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

				ch.confirmSelect(); // Habilitar confirmaciones de publicación
				ch.basicQos(1); // Procesar un mensaje a la vez

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
		publish(topic, event, null);

	}

	@Override
	public void publish(String topic, Object event, String partitionKey) {
		try {
			channel.exchangeDeclare(topic, "fanout", true);
			String message = serializer.serialize(event);
			channel.basicPublish(topic, "", null, message.getBytes());
		} catch (Exception e) {
			throw new RuntimeException("Error publishing event to RabbitMQ: " + e.getMessage(), e);
		}
	}

	@Override
	public void publish(String topic, String routingKey, Object event) {
		System.out
				.println("----- [RabbitMQEventBus] Publishing to topic: " + topic + " with routing key: " + routingKey);
		try {
			channel.exchangeDeclare(topic, "topic", true);
			String message = serializer.serialize(event);
			channel.basicPublish(topic, routingKey, null, message.getBytes());
		} catch (Exception e) {
			throw new RuntimeException("Error publishing event to RabbitMQ: " + e.getMessage(), e);
		}
	}

	@Override
	public <T> void subscribe(String topic, Class<T> eventType, EventHandler<T> handler, String consumerGroup) {
		subscribe(topic, "#", eventType, handler);
	}

	@Override
	public <T> void subscribe(String topic, String routingKeyOrPattern, Class<T> eventType, EventHandler<T> handler) {
		System.out
				.println("----- [RabbitMQEventBus] Subscribing to topic: " + topic + " with routing key: "
						+ routingKeyOrPattern);
		try {
			channel.exchangeDeclare(topic, "topic", true);
			String dlxExchange = topic + ".dlx";
			channel.exchangeDeclare(dlxExchange, "fanout", true);

			Map<String, Object> queueArgs = new HashMap<>();
			queueArgs.put("x-dead-letter-exchange", dlxExchange);
			String queue = channel.queueDeclare("", true, false, true, queueArgs).getQueue();

			channel.queueBind(queue, topic, routingKeyOrPattern);

			channel.basicConsume(queue, false, (consumerTag, delivery) -> {
				try {
					String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
					T event = serializer.deserialize(message, eventType);
					handler.handle(event);

					channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

				} catch (Exception e) {
					channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
					System.err.println("Error processing message: " + e.getMessage());
					// Aquí podríamos agregar lógica adicional para manejar el error, como
					// reintentos o alertas
				}
			}, consumerTag -> {
			});

		} catch (Exception e) {
			throw new RuntimeException("Error subscribing to RabbitMQ: " + e.getMessage(), e);
		}
	}

	@Override
	public <T> void onDeadLetter(String topic, Class<T> eventType, EventHandler<T> handler) {
		try {
			String dlxExchange = topic + ".dlx";
			String dlqName = topic + ".dlq";

			channel.exchangeDeclare(dlxExchange, "fanout", true);
			channel.queueDeclare(dlqName, true, false, false, null);
			channel.queueBind(dlqName, dlxExchange, "#");

			channel.basicConsume(dlqName, false, (consumerTag, delivery) -> {
				try {
					String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
					T event = serializer.deserialize(message, eventType);
					handler.handle(event);

					channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
				} catch (Exception e) {
					channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
					System.err.println("Error processing dead letter message: " + e.getMessage());
				}
			}, consumerTag -> {
			});
			System.out.println("Subscribed to RabbitMQ dead letter queue for topic: " + topic);
		} catch (Exception e) {
			throw new RuntimeException("Error subscribing to RabbitMQ dead letter queue: " + e.getMessage(), e);
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
