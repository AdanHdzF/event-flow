package com.edacourse.api.domain.event;

public record OrderCreatedEvent(String customerId, String id, String product, double price, int quantity) {
}
