package app.persistence;

import app.entities.Order;

import java.util.List;

public class OrderMapper {
    public List<Order> getAllOrders(ConnectionPool connectionpool){
        return null;
    }

    public List<Order> getAllOrdersBySTatus(String orderStatus, ConnectionPool connectionPool){
        return null;
    }

    public List<Order> getAllOrdersByUser(int userId, ConnectionPool connectionPool){
        return null;
    }

    public void createOrder(Order order, ConnectionPool connectionPool){

    }

    public void deleteOrder(int orderId, ConnectionPool connectionPool){

    }

    public void setOrderStatus(int orderId,String orderStatus, ConnectionPool connectionPool){

    }

    public Order getAllOrdersById(int orderId, ConnectionPool connectionPool){
        return null;
    }
}
