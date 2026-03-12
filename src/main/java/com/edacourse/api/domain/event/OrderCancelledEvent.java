package com.edacourse.api.domain.event;

public record OrderCancelledEvent(String id, String reason) {
}
