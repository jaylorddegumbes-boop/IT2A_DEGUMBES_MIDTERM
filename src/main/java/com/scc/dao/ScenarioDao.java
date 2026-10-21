package com.scc.dao;

import com.scc.model.ScenarioEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ScenarioDao {

    private static final String CREATE_TABLE_SQL =
            "CREATE TABLE IF NOT EXISTS tickets ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "attendee_name TEXT NOT NULL, "
                    + "ticket_tier TEXT NOT NULL, "
                    + "price REAL NOT NULL, "
                    + "payment TEXT NOT NULL)";

    private static final String INSERT_SQL =
            "INSERT INTO tickets (attendee_name, ticket_tier, price, payment) VALUES (?, ?, ?, ?)";

    private static final String SELECT_ALL_SQL =
            "SELECT id, attendee_name, ticket_tier, price, payment FROM tickets ORDER BY id";

    public ScenarioDao() {
        createTable();
    }

    private void createTable() {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(CREATE_TABLE_SQL);
        } catch (SQLException e) {
            System.out.println("Database initialization error: " + e.getMessage());
        }
    }

    public boolean insert(ScenarioEntity entity) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {
            statement.setString(1, entity.attendeeName());
            statement.setString(2, entity.ticketTier());
            statement.setDouble(3, entity.price());
            statement.setString(4, entity.payment());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Insert error: " + e.getMessage());
            return false;
        }
    }

    public List<ScenarioEntity> findAll() {
        List<ScenarioEntity> tickets = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                tickets.add(new ScenarioEntity(
                        resultSet.getInt("id"),
                        resultSet.getString("attendee_name"),
                        resultSet.getString("ticket_tier"),
                        resultSet.getDouble("price"),
                        resultSet.getString("payment")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Fetch error: " + e.getMessage());
        }
        return tickets;
    }
}