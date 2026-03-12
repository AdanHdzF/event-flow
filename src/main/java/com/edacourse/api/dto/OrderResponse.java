package com.edacourse.api.dto;

import java.time.Instant;

import com.edacourse.api.domain.Order;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderResponse {
	private final String id;
	private final String customerId;
	private final String product;
	private final double price;
	private final int quantity;
	private final Instant createdAt;

	@JsonCreator
	public OrderResponse(
			@JsonProperty("id") String id,
			@JsonProperty("customer_id") String customerId,
			@JsonProperty("product") String product,
			@JsonProperty("price") double price,
			@JsonProperty("quantity") int quantity,
			@JsonProperty("created_at") Instant createdAt) {
		this.id = id;
		this.customerId = customerId;
		this.product = product;
		this.price = price;
		this.quantity = quantity;
		this.createdAt = createdAt;
	}

	public static OrderResponse from(Order order) {
		return new OrderResponse(
				order.getId(),
				order.getCustomerId(),
				order.getProduct(),
				order.getPrice(),
				order.getQuantity(),
				order.getCreatedAt());
	}

	public String getId() {
		return id;
	}

	public String getProduct() {
		return product;
	}

	public double getPrice() {
		return price;
	}

	public int getQuantity() {
		return quantity;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

}
