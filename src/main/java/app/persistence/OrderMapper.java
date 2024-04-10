package app.persistence;

import app.entities.Order;
import app.entities.OrderItem;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    public static List<Order> getAllOrders(ConnectionPool connectionpool) throws DatabaseException {
        List<Order> orderList = new ArrayList<>();
        String sql = "SELECT order_id, user_id, order_status, order_timestamp FROM orders";

        try (
                Connection connection = connectionpool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                int userId = rs.getInt("user_id");
                String orderStatus = rs.getString("order_status");
                LocalDateTime timestamp = rs.getTimestamp("order_timestamp").toLocalDateTime();
                orderList.add(new Order(orderId, userId, getAllOrderItems(orderId, connectionpool), orderStatus, timestamp));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while getting all orders: " + e.getMessage());
        }
        return orderList;
    }

    public static List<Order> getAllOrdersByStatus(String orderStatus, ConnectionPool connectionPool) throws DatabaseException {
        List<Order> orderList = new ArrayList<>();
        String sql = "SELECT order_id, user_id, order_timestamp FROM orders WHERE order_status = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setString(1, orderStatus);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                int userId = rs.getInt("user_id");
                LocalDateTime timestamp = rs.getTimestamp("order_timestamp").toLocalDateTime();
                orderList.add(new Order(orderId, userId, getAllOrderItems(orderId, connectionPool), orderStatus, timestamp));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while getting all orders by status: " + e.getMessage());
        }
        return orderList;
    }

    public static List<Order> getAllUserOrders(int userId, ConnectionPool connectionPool) throws DatabaseException {
        List<Order> orderList = new ArrayList<>();
        String sql = "SELECT order_id, order_status, order_timestamp FROM orders where user_id = ? order by order_id";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                String orderStatus = rs.getString("order_status");
                LocalDateTime timestamp = rs.getTimestamp("order_timestamp").toLocalDateTime();
                orderList.add(new Order(orderId, userId, getAllOrderItems(orderId, connectionPool), orderStatus, timestamp));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while getting all user orders: " + e.getMessage());
        }
        return orderList;
    }

    public static void createOrder(Order order, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO orders (user_id, order_status, order_timestamp) values (?,?,?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, order.getUserId());
            ps.setString(2, order.getStatus());
            ps.setTimestamp(3, Timestamp.valueOf(order.getTimestamp()));
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                int newOrderId = rs.getInt(1);

                for (OrderItem orderItem : order.getOrderItems()) {
                    orderItem.setOrderId(newOrderId);
                    createOrderItem(orderItem, connectionPool);
                }
            } else {
                throw new DatabaseException("Error could not insert order");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error in DB connection", e.getMessage());
        }

    }

    public static void deleteOrder(int orderId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "DELETE FROM orders WHERE order_id = ?";

        deleteOrderItems(orderId, connectionPool);

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, orderId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Error could not delete order");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database error", e.getMessage());
        }
    }

    public static void setOrderStatus(int orderId, String orderStatus, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE orders SET order_status = ? WHERE order_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, orderStatus);
            ps.setInt(2, orderId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Error updating order status");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error in DB");
        }
    }

    public static Order getOrderById(int orderId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT user_id, order_status, order_timestamp " +
                " FROM orders WHERE order_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String orderStatus = rs.getString("order_status");
                LocalDateTime timestamp = rs.getTimestamp("order_timestamp").toLocalDateTime();

                return new Order(orderId, userId, getAllOrderItems(orderId, connectionPool), orderStatus, timestamp);
            } else {
                throw new DatabaseException("Could not get order with id = " + orderId);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Database Error", e.getMessage());
        }
    }

    private static List<OrderItem> getAllOrderItems(int orderId, ConnectionPool connectionPool) throws SQLException, DatabaseException {
        List<OrderItem> orderItems = new ArrayList<>();

        String sql = "SELECT order_item_id, cupcake_bottom_id, cupcake_top_id, order_item_quantity FROM order_items WHERE order_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int orderItemId = rs.getInt("order_item_id");
                int cupcakeBottomId = rs.getInt("cupcake_bottom_id");
                int cupcakeTopId = rs.getInt("cupcake_top_id");
                int orderItemQuantity = rs.getInt("order_item_quantity");
                orderItems.add(new OrderItem(orderItemId, orderId, CupcakeMapper.getCupcakeBottomById(cupcakeBottomId, connectionPool), CupcakeMapper.getCupcakeTopById(cupcakeTopId, connectionPool), orderItemQuantity));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to get all order items");
        }
        return orderItems;
    }

    private static void createOrderItem(OrderItem orderItem, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "INSERT INTO order_items (order_id, cupcake_bottom_id, cupcake_top_id, order_item_quantity) values (?,?,?,?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, orderItem.getOrderId());
            ps.setInt(2, orderItem.getCupcakeBottom().getCupcakePartId());
            ps.setInt(3, orderItem.getCupcakeTop().getCupcakePartId());
            ps.setInt(4, orderItem.getQuantity());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Error could not insert order item");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error in DB connection", e.getMessage());
        }
    }

    private static void deleteOrderItems(int orderId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "DELETE FROM order_items WHERE order_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, orderId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new DatabaseException("Error trying to delete order items");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error in deleting orderItem", e.getMessage());
        }
    }

    private static OrderItem getOrderItemById(int orderItemId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT order_id, cupcake_bottom_id, cupcake_top_id, order_item_quantity " +
                " FROM order_items WHERE order_id = ?";

        OrderItem orderItem = null;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int orderId = rs.getInt("order_id");
                int cupcakeBottomId = rs.getInt("cupcake_bottom_id");
                int cupcakeTopId = rs.getInt("cupcake_top_id");
                int orderItemQuantity = rs.getInt("order_item_quantity");
                orderItem = new OrderItem(orderItemId, orderId, CupcakeMapper.getCupcakeBottomById(cupcakeBottomId, connectionPool), CupcakeMapper.getCupcakeTopById(cupcakeTopId, connectionPool), orderItemQuantity);
            } else {
                throw new DatabaseException("Could not get order item with id = " + orderItemId);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Database Error", e.getMessage());
        }
        return orderItem;
    }
}
