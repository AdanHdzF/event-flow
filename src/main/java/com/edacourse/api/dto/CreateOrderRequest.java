package com.edacourse.api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateOrderRequest {
	private final String customerId;
	private final String product;
	private final double price;
	private final int quantity;

	@JsonCreator
	public CreateOrderRequest(
			@JsonProperty("customer_id") String customerId,
			@JsonProperty("product") String product,
			@JsonProperty("price") double price,
			@JsonProperty("quantity") int quantity) {
		this.customerId = customerId;
		this.product = product;
		this.price = price;
		this.quantity = quantity;
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

}
