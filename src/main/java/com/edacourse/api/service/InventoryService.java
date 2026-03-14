package com.edacourse.api.service;

import com.edacourse.api.domain.event.InventoryReservedEvent;
import com.edacourse.api.infrastructure.messaging.RoutablePublisher;

public class InventoryService {

	private final RoutablePublisher eventBus;

	public InventoryService(RoutablePublisher eventBus) {
		this.eventBus = eventBus;
	}

	public void updateInventory(String product, int quantity) {
		System.out.println("[InventoryService] Inventario actualizado: " + product + ", Cantidad: " + quantity);

		eventBus.publish("inventory.reserved", "inventory.reserved", new InventoryReservedEvent(product, quantity));
	}
}
