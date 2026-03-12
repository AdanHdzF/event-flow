package com.edacourse.api.infrastructure.messaging;

public record OrderCancelledEvent(String id, String reason) {
}
