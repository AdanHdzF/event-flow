package com.edacourse.api.infrastructure.messaging;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class InMemoryEventBus implements EventBus {

	private final EventSerializer serializer;
	private final Map<String, List<Subscription>> topics = new ConcurrentHashMap<>();

	public InMemoryEventBus(EventSerializer serializer) {
		this.serializer = Objects.requireNonNull(serializer, "serializer cannot be null");
		System.out.println("Connected to memory event bus");
	}

	@Override
	public void publish(String topic, Object event) {
		Objects.requireNonNull(topic, "topic cannot be null");
		Objects.requireNonNull(event, "event cannot be null");

		List<Subscription> handlers = topics.get(topic);
		if (handlers == null || handlers.isEmpty()) {
			return;
		}

		// Serialize once and deliver a deserialized instance per subscriber type
		String json = serializer.serialize(event);

		for (Subscription sub : handlers) {
			try {
				// System.out
				// .println("[InMemoryEventBus] Delivering event to handler for topic '" + topic
				// + "': " + event);
				deliverToSubscriber(json, sub);
			} catch (Exception e) {
				System.err.println("Error delivering event to handler for topic '" + topic + "': " + e.getMessage());
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void deliverToSubscriber(String json, Subscription sub) {
		Object deserialized = serializer.deserialize(json, (Class<Object>) sub.eventType);
		((EventHandler<Object>) sub.handler).handle(deserialized);
	}

	@Override
	public <T> void subscribe(String topic, Class<T> eventType, EventHandler<T> handler) {
		Objects.requireNonNull(topic, "topic cannot be null");
		Objects.requireNonNull(eventType, "eventType cannot be null");
		Objects.requireNonNull(handler, "handler cannot be null");

		// System.out.println("[InMemoryEventBus] Subscribing handler for topic '" +
		// topic + "' and event type "
		// + eventType.getSimpleName());

		List<Subscription> handlers = topics.computeIfAbsent(topic, k -> new CopyOnWriteArrayList<>());
		handlers.add(new Subscription(eventType, handler));
	}

	@Override
	public void close() {
		topics.clear();
		System.out.println("MemoryEventBus closed");
	}

	private static class Subscription {
		final Class<?> eventType;
		final EventHandler<?> handler;

		Subscription(Class<?> eventType, EventHandler<?> handler) {
			this.eventType = eventType;
			this.handler = handler;
		}
	}

}
