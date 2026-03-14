package com.edacourse.api.subscriber;

import com.edacourse.api.domain.event.InventoryReservedEvent;
// import com.edacourse.api.domain.event.OrderCancelledEvent;
import com.edacourse.api.domain.event.OrderCreatedEvent;
import com.edacourse.api.domain.event.PaymentCompletedEvent;
import com.edacourse.api.infrastructure.messaging.RoutableSubscriber;
import com.edacourse.api.service.AnalyticsService;

public class AnalyticsSubscriber {
	// private static final String CONSUMER_GROUP = "analytics-service-group";
	private final AnalyticsService analyticsService;

	public AnalyticsSubscriber(RoutableSubscriber eventBus, AnalyticsService analyticsService) {
		this.analyticsService = analyticsService;
		eventBus.subscribe("orders.created", "#", OrderCreatedEvent.class, this::onOrderCreated);
		eventBus.subscribe("inventory.reserved", "#", InventoryReservedEvent.class,
				this::onReservedInventory);
		eventBus.subscribe("payment.completed", "#", PaymentCompletedEvent.class,
				this::onProcessPayment);
	}

	private void onOrderCreated(OrderCreatedEvent event) {
		// System.out.println("[AnalyticsSubscriber] onOrderCreated");
		analyticsService.saveEvent("Order created: " + event.id());
	}

	private void onReservedInventory(InventoryReservedEvent event) {
		// System.out.println("[AnalyticsSubscriber] onInventoryReserved");
		analyticsService.saveEvent("Inventory reserved: " + event.product());
	}

	private void onProcessPayment(PaymentCompletedEvent event) {
		// System.out.println("[AnalyticsSubscriber] onProcessPayment");
		analyticsService.saveEvent("Payment completed: " + event.orderId());
	}

}
