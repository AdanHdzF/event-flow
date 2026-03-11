package com.edacourse.api.repository;

import java.util.List;
import java.util.Optional;

import com.edacourse.api.domain.Order;

public interface OrderRepository {
	void save(Order order);

	Optional<Order> findById(String id);

	List<Order> findAll();
}
