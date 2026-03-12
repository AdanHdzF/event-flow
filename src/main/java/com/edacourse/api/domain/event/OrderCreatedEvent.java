package com.edacourse.api.domain.event;

public record OrderCreatedEvent(String customerId, String product, double price, int quantity) {
}
