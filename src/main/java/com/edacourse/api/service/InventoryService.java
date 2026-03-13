package com.edacourse.api.service;

import com.edacourse.api.domain.event.InventoryReservedEvent;
import com.edacourse.api.infrastructure.messaging.EventBus;

public class InventoryService {

	private final EventBus eventBus;

	public InventoryService(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public void updateInventory(String product, int quantity) {
		System.out.println("[InventoryService] Inventario actualizado: " + product + ", Cantidad: " + quantity);

		eventBus.publish("inventory.reserved", new InventoryReservedEvent(product, quantity), null);
	}
}
