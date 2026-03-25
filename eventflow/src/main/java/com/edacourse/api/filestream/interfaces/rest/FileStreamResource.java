package com.edacourse.api.filestream.interfaces.rest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.edacourse.api.backup.domain.dto.BackupResponseDTO;
import com.edacourse.api.filestream.application.service.FileStreamService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/files")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FileStreamResource {
	@Inject
	private FileStreamService fileStreamService;

	/**
	 * POST /api/files/import?count=10000 — Genera CSV de prueba y lo envia por
	 * chunks via Kafka
	 */
	@POST
	@Path("/import")
	public Response importCatalog(@QueryParam("count") @DefaultValue("1000") int count) {
		if (count < 1 || count > 50000) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("{\"error\":\"count debe estar entre 1 y 50000\"}")
					.build();
		}

		try {
			// Generar CSV de prueba
			String csvPath = fileStreamService.generateTestCsv(count, "/data/imports");

			// Dividir en chunks y enviar via Kafka
			String fileId = fileStreamService.sendFile(csvPath);

			return Response.accepted()
					.entity("{\"fileId\":\"" + fileId + "\",\"products\":" + count
							+ ",\"status\":\"streaming\",\"message\":\"Archivo dividido en chunks y enviado a Kafka. El consumidor reensamblara e importara.\"}")
					.build();
		} catch (Exception e) {
			return Response.serverError()
					.entity("{\"error\":\"" + e.getMessage() + "\"}")
					.build();
		}
	}

	@POST
	@Path("/import-file")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(
			@FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {

		if (fileInputStream == null || fileDetail == null || fileDetail.getFileName() == null) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(new BackupResponseDTO("", "No file provided")).build();
		}

		String fileName = fileDetail.getFileName();
		String uploadDir = "/data/imports";
		new File(uploadDir).mkdirs();
		String filePath = uploadDir + "/" + fileName;

		try {
			Files.copy(fileInputStream, new File(filePath).toPath(),
					StandardCopyOption.REPLACE_EXISTING);

			// Dividir en chunks y enviar via Kafka
			String fileId = fileStreamService.sendFile(filePath);

			return Response.accepted()
					.entity("{\"fileId\":\"" + fileId + "\",\"filePath\":\"" + filePath + "\""
							+ ",\"status\":\"streaming\",\"message\":\"Archivo dividido en chunks y enviado a Kafka. El consumidor reensamblara e importara.\"}")
					.build();
		} catch (IOException e) {
			return Response.serverError()
					.entity(new BackupResponseDTO("", "Failed to save file: " +
							e.getMessage()))
					.build();
		}

		// String backupId = backupService.requestFileBackup(fileName, filePath);
		// return Response.ok(new BackupResponseDTO(backupId, "File backup requested
		// for: " + fileName)).build();
	}
}
