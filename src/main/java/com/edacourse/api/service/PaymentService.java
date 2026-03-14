package com.edacourse.api.service;

import com.edacourse.api.domain.event.PaymentCompletedEvent;
import com.edacourse.api.infrastructure.messaging.RoutablePublisher;

public class PaymentService {

	private final RoutablePublisher eventBus;

	public PaymentService(RoutablePublisher eventBus) {
		this.eventBus = eventBus;
	}

	public void processPayment(String orderId) {
		System.out.println("[PaymentService] Pago procesado [Order ID: " + orderId + "]");

		eventBus.publish("payment.completed", "payment.completed", new PaymentCompletedEvent(orderId));
	}
}
