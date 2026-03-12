package com.edacourse.api.domain;

import java.time.Instant;
import java.util.UUID;

public class Order {
	public enum Status {
		CREATED, PENDING, PAID, SHIPPED, DELIVERED, CANCELLED
	}

	private final String id;
	private final String customerId;
	private final String product;
	private final double price;
	private final int quantity;
	private Status status;
	private final Instant createdAt;
	private Instant cancelledAt;
	private String cancelReason;

	public Order(String customerId, String product, double price, int quantity) {
		this.id = UUID.randomUUID().toString().substring(0, 8);
		this.customerId = customerId;
		this.product = product;
		this.price = price;
		this.quantity = quantity;
		this.status = Status.CREATED;
		this.createdAt = Instant.now();
		this.cancelledAt = null;
		this.cancelReason = null;
	}

	public void cancel(String reason) {
		if (status == Status.CANCELLED)
			throw new IllegalStateException("Order is already cancelled");

		this.status = Status.CANCELLED;
		this.cancelledAt = Instant.now();
		this.cancelReason = reason;
	}

	public String getId() {
		return id;
	}

	public String getCustomerId() {
		return customerId;
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

	public Status getStatus() {
		return status;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getCancelledAt() {
		return cancelledAt;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	@Override
	public String toString() {
		return "Order{id='" + id + "', product='" + product + "', price=" + price + ", quantity=" + quantity
				+ ", status=" + status + ", createdAt=" + createdAt + ", cancelledAt=" + cancelledAt
				+ ", cancelReason='" + cancelReason + "'}";
	}

}
