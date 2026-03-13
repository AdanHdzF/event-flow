package com.edacourse.api.subscriber;

import com.edacourse.api.domain.event.OrderCancelledEvent;
import com.edacourse.api.domain.event.OrderCreatedEvent;
import com.edacourse.api.infrastructure.messaging.EventBus;
import com.edacourse.api.service.InventoryService;

public class InventorySubscriber {
	private final InventoryService inventoryService;

	public InventorySubscriber(EventBus eventBus, InventoryService inventoryService) {
		this.inventoryService = inventoryService;
		eventBus.subscribe("orders.created", OrderCreatedEvent.class, this::onOrderCreated);
		eventBus.subscribe("orders.cancelled", OrderCancelledEvent.class, this::onOrderCancelled);
	}

	private void onOrderCreated(OrderCreatedEvent event) {
		System.out.println("[InventorySubcriber] onOrderCreated");
		inventoryService.updateInventory(event.product(), event.quantity());
	}

	private void onOrderCancelled(OrderCancelledEvent event) {
		System.out.println("[InventorySubcriber] onOrderCancelled");
	}
}
