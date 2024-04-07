package app.entities;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private int orderId;
    private int userId;
    private List<OrderItem> orderItems;
    private String status;
    private LocalDateTime timestamp;

    public Order(int orderId, int userId, List<OrderItem> orderItems, String status, LocalDateTime timestamp) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderItems = orderItems;
        this.status = status;
        this.timestamp = timestamp;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getUserId() {
        return userId;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", orderItems=" + orderItems +
                ", status='" + status + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
