package app.persistence;

import app.entities.CupcakePart;
import app.entities.Order;
import app.entities.OrderItem;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderMapper {
    public static List<Order> getAllOrders(ConnectionPool connectionpool) {
        String sql = "SELECT order_id, user_id, order_status, order_timestamp FROM public.orders";

        return null;
    }

    public static List<Order> getAllOrdersByStatus(String orderStatus, ConnectionPool connectionPool) {
        return null;
    }

    public static List<Order> getAllUserOrders(int userId, ConnectionPool connectionPool) {
        return null;
    }

    public static void createOrder(Order order, ConnectionPool connectionPool) {

    }

    public static void deleteOrder(int orderId, ConnectionPool connectionPool) {

    }

    public static void setOrderStatus(int orderId, String orderStatus, ConnectionPool connectionPool) {

    }

    public static Order getOrderById(int orderId, ConnectionPool connectionPool) {
        return null;
    }

    private static List<OrderItem> getAllOrderItems(int orderItem, ConnectionPool connectionPool) {
        return null;
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

    private static void deleteOrderItem(int orderItemId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "delete from order_items where order_items.order_item_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, orderItemId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Error in updating order items");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error in deleting orderItem", e.getMessage());
        }
    }

    private static OrderItem getOrderItemById(int orderItemId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT order_item_id, order_id, cupcake_bottom_id, cupcake_top_id, order_item_quantity " +
                " FROM order_items WHERE order_id = ?";

        OrderItem orderItem = null;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int ordersItemId = rs.getInt("order_item_id");
                int orderId = rs.getInt("order_id");
                int cupcakeBottomId = rs.getInt("cupcake_bottom_id");
                int cupcakeTopId = rs.getInt("cupcake_top_id");
                int orderItemQuantity = rs.getInt("order_item_quantity");
                orderItem = new OrderItem(ordersItemId, orderId, CupcakeMapper.getCupcakePartById(cupcakeTopId, connectionPool, CupcakePart.Type.TOP), CupcakeMapper.getCupcakePartById(cupcakeBottomId, connectionPool, CupcakePart.Type.BOTTOM), orderItemQuantity);
            } else {
                throw new DatabaseException("The order id doesn't exist");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Database Error", e.getMessage());
        }
        return orderItem;
    }
}
