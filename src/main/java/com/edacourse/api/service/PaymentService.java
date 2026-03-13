package com.edacourse.api.service;

import com.edacourse.api.domain.event.PaymentCompletedEvent;
import com.edacourse.api.infrastructure.messaging.EventBus;

public class PaymentService {

	private final EventBus eventBus;

	public PaymentService(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public void processPayment(String orderId) {
		System.out.println("[PaymentService] Pago procesado [Order ID: " + orderId + "]");

		eventBus.publish("payment.completed", new PaymentCompletedEvent(orderId), null);
	}
}
