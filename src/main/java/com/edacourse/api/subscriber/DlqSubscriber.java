package com.edacourse.api.subscriber;

import com.edacourse.api.domain.event.OrderCreatedEvent;
import com.edacourse.api.infrastructure.messaging.DeadLetterHandler;

public class DlqSubscriber {

	public DlqSubscriber(DeadLetterHandler deadLetterHandler) {
		deadLetterHandler.onDeadLetter("orders.created", OrderCreatedEvent.class, this::onDeadLetter);
	}

	public void onDeadLetter(OrderCreatedEvent event) {
		System.out.println(
				"Received dead letter for topic: orders.created, event type: " + event.getClass().getSimpleName());
		// Aquí podrías implementar lógica adicional, como almacenar el evento en una
		// base de datos para su posterior análisis
	}

}
