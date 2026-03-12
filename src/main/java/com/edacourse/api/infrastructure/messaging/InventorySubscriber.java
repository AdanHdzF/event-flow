package com.edacourse.api.infrastructure.messaging;

public class InventorySubscriber {
	public InventorySubscriber(EventBus eventBus) {
		eventBus.subscribe("orders.created", OrderCreatedEvent.class, this::onOrderCreated);
		eventBus.subscribe("orders.cancelled", OrderCancelledEvent.class, this::onOrderCancelled);
	}

	private void onOrderCreated(OrderCreatedEvent event) {
		System.out.println("[InventorySubcriber] Order created: " + event);
	}

	private void onOrderCancelled(OrderCancelledEvent event) {
		System.out.println("[InventorySubcriber] Order cancelled: " + event);
	}
}
