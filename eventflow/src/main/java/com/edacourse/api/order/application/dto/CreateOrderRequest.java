package com.edacourse.api.order.application.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateOrderRequest {
	private final String customerId;
	private final List<OrderItemRequest> items;

	@JsonCreator
	public CreateOrderRequest(
			@JsonProperty("customer_id") String customerId,
			@JsonProperty("items") List<OrderItemRequest> items) {
		this.customerId = customerId;
		this.items = items;
	}

	public String getCustomerId() {
		return customerId;
	}

	public List<OrderItemRequest> getItems() {
		return items;
	}

	public static class OrderItemRequest {
		private final String productId;
		private final String productName;
		private final double price;
		private final int quantity;

		@JsonCreator
		public OrderItemRequest(
				@JsonProperty("product_id") String productId,
				@JsonProperty("product_name") String productName,
				@JsonProperty("price") double price,
				@JsonProperty("quantity") int quantity) {
			this.productId = productId;
			this.productName = productName;
			this.price = price;
			this.quantity = quantity;
		}

		public String getProductId() {
			return productId;
		}

		public String getProductName() {
			return productName;
		}

		public double getPrice() {
			return price;
		}

		public int getQuantity() {
			return quantity;
		}
	}
}
