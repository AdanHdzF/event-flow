package com.edacourse.api.infrastructure.notification;

public class ConsoleNotification implements NotificationService {

	@Override
	public void notify(String message) {
		System.out.println("[ConsoleNotification]: " + message);
	}

}
