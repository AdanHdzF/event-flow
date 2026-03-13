package com.edacourse.api.infrastructure.messaging;

public interface DeadLetterHandler {
	void onDeadLetter(String topic, Class<?> eventType, EventHandler<?> handler);
}
