package com.edacourse.api.subscriber;

// import com.edacourse.api.domain.event.OrderCancelledEvent;
import com.edacourse.api.domain.event.OrderCreatedEvent;
import com.edacourse.api.infrastructure.messaging.EventBus;
import com.edacourse.api.service.PaymentService;

public class PaymentSubscriber {
	private final PaymentService paymentService;

	public PaymentSubscriber(EventBus eventBus, PaymentService paymentService) {
		this.paymentService = paymentService;
		eventBus.subscribe("orders.created", OrderCreatedEvent.class, this::onOrderCreated);
		// eventBus.subscribe("orders.cancelled", OrderCancelledEvent.class,
		// this::onOrderCancelled);
	}

	private void onOrderCreated(OrderCreatedEvent event) {
		System.out.println("[PaymentSubscriber] onOrderCreated");
		paymentService.processPayment(event.id());
	}

	// private void onOrderCancelled(OrderCancelledEvent event) {
	// System.out.println("[PaymentSubscriber] Order cancelled: " + event);
	// }
}
