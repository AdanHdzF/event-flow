package com.edacourse.api.shared.infrastructure.interfaces.rest;

import java.io.InputStream;

import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Singleton
@Path("/")
public class StaticFileResource {

	@GET
	@Path("sse-test")
	@Produces(MediaType.TEXT_HTML)
	public Response sseTestPage() {

		InputStream htmlStream = getClass().getClassLoader().getResourceAsStream("static/sse-test.html");
		if (htmlStream == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("Archivo no encontrado").build();
		}

		return Response.ok(htmlStream).build();
	}

}
