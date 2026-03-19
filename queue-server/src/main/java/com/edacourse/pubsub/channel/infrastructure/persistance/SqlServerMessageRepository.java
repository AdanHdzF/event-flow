package com.edacourse.pubsub.channel.infrastructure.persistance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.edacourse.pubsub.channel.model.Message;
import com.edacourse.pubsub.channel.repository.MessageRepository;

public class SqlServerMessageRepository implements MessageRepository {

	private final String jdbcUrl;
	private final String username;
	private final String password;

	public SqlServerMessageRepository() {
		this.jdbcUrl = System.getenv("DB_URL");
		this.username = System.getenv("DB_USER");
		this.password = System.getenv("DB_PASSWORD");
	}

	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(jdbcUrl, username, password);
	}

	@Override
	public Message save(Message message) {
		String sql = "INSERT INTO messages (id, channel_id, payload, publisher_id) VALUES (?, ?, ?, ?)";
		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, message.getId());
			ps.setString(2, message.getChannelId());
			ps.setString(3, message.getPayload());
			ps.setString(4, message.getPublisherId());
			ps.executeUpdate();

			try (ResultSet keys = ps.getGeneratedKeys()) {
				if (keys.next()) {
					message.setId(keys.getString(1));
				}
			}
			System.out.println("[PUBSUB] Mensaje guardado: id=" + message.getId() + " canal=" + message.getChannelId());
			return message;
		} catch (SQLException e) {
			throw new RuntimeException("Error guardando mensaje: " + e.getMessage(), e);
		}
	}

	@Override
	public List<Message> findByChannelId(String channelId, int limit) {
		String sql = "SELECT TOP (?) id, channel_id, payload, publisher_id, published_at FROM messages WHERE channel_id = ? ORDER BY published_at DESC";
		List<Message> messages = new ArrayList<>();
		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, limit);
			ps.setString(2, channelId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					messages.add(mapRow(rs));
				}
			}
			return messages;
		} catch (SQLException e) {
			throw new RuntimeException("Error listando mensajes: " + e.getMessage(), e);
		}
	}

	private Message mapRow(ResultSet rs) throws SQLException {
		return new Message(
				rs.getString("id"),
				rs.getString("channel_id"),
				rs.getString("payload"),
				rs.getString("publisher_id"),
				rs.getTimestamp("published_at").toInstant());
	}

}