package com.edacourse.api.shared.infrastructure.sse;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;

import jakarta.inject.Singleton;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseBroadcaster;
import jakarta.ws.rs.sse.SseEventSink;

@Singleton
public class EventSseBroadcaster {

	private SseBroadcaster broadcaster;
	private Sse sse;
	private final AtomicLong eventCounter = new AtomicLong(0);
	private final ConcurrentLinkedDeque<SseEventRecord> eventHistory = new ConcurrentLinkedDeque<>();
	private static final int MAX_HISTORY_SIZE = 100;

	public void initialize(Sse sse) {
		this.sse = sse;
		this.broadcaster = sse.newBroadcaster();
		this.broadcaster.onClose(sink -> {
			System.out.println("Cliente desconectado: " + sink);
		});

		System.out.println("SSE Broadcaster inicializado");
	}

	public boolean isReady() {
		return broadcaster != null && sse != null;
	}

	public void register(SseEventSink sink, String lastEventId) {
		if (!isReady())
			throw new IllegalStateException("SSE Broadcaster no está listo");

		if (lastEventId != null && !lastEventId.isBlank()) {
			replayEvents(sink, lastEventId);
		}

		broadcaster.register(sink);
		System.out.println("Cliente registrado: " + sink);
	}

	public void broadcast(String eventType, String topic, String data) {
		if (!isReady())
			throw new IllegalStateException("SSE Broadcaster no está listo");

		long id = eventCounter.incrementAndGet();
		String eventId = String.valueOf(id);

		OutboudSseEvent event = sse.newEventBuilder()
				.id(eventId)
				.name(eventType)
				.data(String.class, "{\"topic\":\"" + topic + "\",\"data\":\"" + data + "\"}")
				.comment(topic)
				.build();

		eventHistory.add(new SseEventRecord(eventId, eventType, topic, data));

		while (eventHistory.size() > MAX_HISTORY_SIZE) {
			eventHistory.pollFirst();
		}

		broadcaster.broadcast(event);
		System.out.println("Evento transmitido: " + eventId + " - " + eventType + " - " + topic + " - " + data);
	}

	private void replayEvents(SseEventSink sink, String lastEventId) {
		long lastId;
		try {
			lastId = Long.parseLong(lastEventId);
		} catch (NumberFormatException e) {
			System.out.println("ID de evento no válido para replay: " + lastEventId);
			e.printStackTrace();
			return;
		}

		int replayed = 0;
		for (SseEventRecord record : eventHistory) {
			long recordId = Long.parseLong(record.getId());
			if (recordId > lastId) {
				OutboudSseEvent event = sse.newEventBuilder()
						.id(record.getId())
						.name(record.getEventType())
						.data(String.class,
								"{\"topic\":\"" + record.getTopic() + "\",\"data\":\"" + record.getData() + "\"}")
						.comment(record.getTopic())
						.build();
				sink.send(event);
				replayed++;
			}
		}

		if (replayed > 0) {
			System.out.println("Reproduciendo " + replayed + " eventos para cliente: " + sink);
		} else {
			System.out.println("No hay eventos nuevos para reproducir para cliente: " + sink);
		}

	}

	public long getEventCount() {
		return eventCounter.get();
	}

	public int getHistorySize() {
		return eventHistory.size();
	}

	public record SseEventRecord(String id, String eventType, String topic, String data) {
	}

}
