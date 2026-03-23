package com.edacourse.api.shared.infrastructure.interfaces.sse;

import com.edacourse.api.shared.infrastructure.sse.EventSseBroadcaster;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseEventSink;

@Singleton
@Path("/api/events")
public class EventSseResource {
	private final EventSseBroadcaster broadcaster;

	@Inject
	public EventSseResource(EventSseBroadcaster broadcaster) {
		this.broadcaster = broadcaster;
	}

	@Context
	public void setSse(Sse sse) {
		if (!broadcaster.isReady()) {
			System.out.println("Inicializando SSE Broadcaster...");
			broadcaster.initialize(sse);
		} else {
			System.out.println("SSE Broadcaster ya está inicializado");
		}
	}

	@GET
	@Path("/stream")
	@Produces(MediaType.SERVER_SENT_EVENTS)
	public void stream(@Context SseEventSink sink,
			@HeaderParam("Last-Event-ID") String lastEventId) {
		broadcaster.register(sink, lastEventId);
	}

}
