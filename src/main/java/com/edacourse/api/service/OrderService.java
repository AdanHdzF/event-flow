package com.edacourse.api.service;

import java.util.List;

// import com.edacourse.api.PromotionService;
import com.edacourse.api.domain.Order;
import com.edacourse.api.domain.Order.Status;
import com.edacourse.api.domain.event.OrderCreatedEvent;
import com.edacourse.api.dto.CreateOrderRequest;
import com.edacourse.api.infrastructure.messaging.EventBus;
import com.edacourse.api.repository.OrderRepository;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class OrderService {
	// private final NotificationService notificationService;
	// private final PromotionService promotionService;
	private final EventBus eventBus;
	private final OrderRepository orderRepository;

	// public OrderService(NotificationService notificationService, PromotionService
	// promotionService, EventBus eventBus) {
	@Inject
	public OrderService(EventBus eventBus, OrderRepository orderRepository) {
		// this.notificationService = notificationService;
		// this.promotionService = promotionService;
		this.eventBus = eventBus;
		this.orderRepository = orderRepository;
	}

	public Order createOrder(CreateOrderRequest dto) {
		String orderDetails = "Producto: " + dto.getProduct() + ", Precio: " + dto.getPrice();
		System.out.println("Orden creada: " + orderDetails);

		// promotionService.applyPromotion(dto.getPrice());
		// notificationService.notify("Nueva orden creada: " + orderDetails);

		Order order = new Order(dto.getCustomerId(), dto.getProduct(), dto.getPrice(), dto.getQuantity());
		orderRepository.save(order);

		eventBus.publish("orders.created",
				new OrderCreatedEvent(order.getCustomerId(), order.getId(), order.getProduct(), order.getPrice(),
						order.getQuantity()),
				order.getCustomerId());

		return order;
	}

	public Order cancelOrder(String id, String reason) {
		String orderDetails = "Producto: " + id;
		System.out.println("Orden cancelada: " + orderDetails);

		// promotionService.applyPromotion(dto.getPrice());
		// notificationService.notify("Orden cancelada: " + orderDetails);

		// Optional<Order> order = orderRepository.findById(id);
		// order.ifPresent(o -> orderRepository.updateStatus(id, Status.CANCELLED,
		// reason));

		orderRepository.updateStatus(id, Status.CANCELLED, reason);

		// return order.orElse(null);

		return null;
	}

	public List<Order> findAllOrders() {
		return orderRepository.findAll();
	}

	public Order findOrderById(String id) {
		return orderRepository.findById(id).orElse(null);
	}

}