package com.edacourse.api.infrastructure.messaging;

public interface RoutableSubscriber extends EventSubscriber {
	void subscribe(String topic, String routingKeyOrPattern, Class<?> eventType, EventHandler<?> handler);
}
