package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserMapper {
    public static User login(String email, String password, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "select user_id, user_role, user_balance from users where user_email=? and user_password=?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("user_id");
                String role = rs.getString("user_role");
                int balance = rs.getInt("user_balance");
                return new User(id, email, password, role, balance);
            } else {
                throw new DatabaseException("Fejl i login. Prøv igen");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database ERROR", e.getMessage());
        }
    }

    public static void createAccount(String email, String password, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO users (user_email, user_password) values (?,?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, email);
            ps.setString(2, password);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl ved oprettelse af ny bruger");
            }
        } catch (SQLException e) {
            String msg = "Der er sket en fejl. Prøv igen";
            if (e.getMessage().startsWith("ERROR: duplicate key value ")) {
                msg = "E-mailen findes allerede. Vælg et andet, eller log ind";
            }
            throw new DatabaseException(msg, e.getMessage());
        }
    }

    public static List<User> getAllUsersByRole(String userRole, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT user_id, user_email, user_password, user_balance FROM users " +
                "WHERE user_role = ?";
        List<User> users = new ArrayList<>();

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, userRole);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int userId = rs.getInt("user_id");
                String userEmail = rs.getString("user_email");
                String userPassword = rs.getString("user_password");
                int userBalance = rs.getInt("user_balance");

                users.add(new User(userId, userEmail, userPassword, userRole, userBalance));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not retrieve data from database", e.getMessage());
        }

        return users;
    }

    public static User getUserById(int userId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT user_email, user_password, user_role, user_balance FROM users WHERE user_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String userEmail = rs.getString("user_email");
                String userPassword = rs.getString("user_password");
                String userRole = rs.getString("user_role");
                int userBalance = rs.getInt("user_balance");

                return new User(userId, userEmail, userPassword, userRole, userBalance);
            } else {
                throw new DatabaseException("Could not get user with id = " + userId);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not retrieve data from database", e.getMessage());
        }
    }

    public static void setUserBalance(int userId, int newUserBalance, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE users SET user_balance = ? WHERE user_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, newUserBalance);
            ps.setInt(2, userId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Failed to update user balance for user ID: " + userId);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error updating user balance", e.getMessage());
        }
    }
}