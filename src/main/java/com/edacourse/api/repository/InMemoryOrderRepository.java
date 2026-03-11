package com.edacourse.api.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.edacourse.api.domain.Order;

public class InMemoryOrderRepository implements OrderRepository {

	private final Map<String, Order> orders = new ConcurrentHashMap<>();

	@Override
	public void save(Order order) {
		System.out.println("Order saved to in-memory repository: " + order);
		orders.put(order.getId(), order);
	}

	@Override
	public Optional<Order> findById(String id) {
		System.out.println("Finding order by ID in in-memory repository: " + id);
		return Optional.ofNullable(orders.get(id));
	}

	@Override
	public List<Order> findAll() {
		System.out.println("Finding all orders in in-memory repository");
		// return List.copyOf(orders.values());
		return new ArrayList<>(orders.values());
	}

}
