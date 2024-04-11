package app.entities;

import java.util.Objects;

public class OrderItem {
    private int orderItemId;
    private int orderId;
    private CupcakePart cupcakeBottom;
    private CupcakePart cupcakeTop;
    private int quantity;

    public OrderItem(int orderItemId, int orderId, CupcakePart cupcakeBottom, CupcakePart cupcakeTop, int quantity) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.cupcakeBottom = cupcakeBottom;
        this.cupcakeTop = cupcakeTop;
        this.quantity = quantity;
    }

    public OrderItem(CupcakePart cupcakeBottom, CupcakePart cupcakeTop, int quantity) {
        this.cupcakeBottom = cupcakeBottom;
        this.cupcakeTop = cupcakeTop;
        this.quantity = quantity;
    }

    public int getTotalItemPrice() {
        int amount = quantity;
        int bottomPart = cupcakeBottom.getPrice();
        int topPart = cupcakeTop.getPrice();
        int totalItemPrice = (bottomPart + topPart) * amount;

        return totalItemPrice;
    }

    public int getOrderItemId() {
        return orderItemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public CupcakePart getCupcakeTop() {
        return cupcakeTop;
    }

    public CupcakePart getCupcakeBottom() {
        return cupcakeBottom;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return getOrderItemId() == orderItem.getOrderItemId() && getOrderId() == orderItem.getOrderId() && getQuantity() == orderItem.getQuantity() && Objects.equals(getCupcakeTop(), orderItem.getCupcakeTop()) && Objects.equals(getCupcakeBottom(), orderItem.getCupcakeBottom());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderItemId(), getOrderId(), getCupcakeTop(), getCupcakeBottom(), getQuantity());
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "orderItemId=" + orderItemId +
                ", orderId=" + orderId +
                ", cupcakeBottom=" + cupcakeBottom +
                ", cupcakeTop=" + cupcakeTop +
                ", quantity=" + quantity +
                '}';
    }
}