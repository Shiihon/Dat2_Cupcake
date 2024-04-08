package app.entities;

import app.persistence.CupcakeMapper;

public class OrderItem {
    private int orderItemId;
    private int orderId;
    private CupcakePart cupcakeTop;
    private CupcakePart cupcakeBottom;
    private int quantity;

    public OrderItem(int orderItemId, int orderId, CupcakePart cupcakeTop, CupcakePart cupcakeBottom, int quantity) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.cupcakeTop = cupcakeTop;
        this.cupcakeBottom = cupcakeBottom;
        this.quantity = quantity;
    }

    public OrderItem(CupcakePart cupcakeTop, CupcakePart cupcakeBottom, int quantity) {
        this.cupcakeTop = cupcakeTop;
        this.cupcakeBottom = cupcakeBottom;
        this.quantity = quantity;
    }

    public int getTotalItemPrice(){
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
    public String toString() {
        return "OrderItem{" +
                "orderItemId=" + orderItemId +
                ", orderId=" + orderId +
                ", cupcakeTop=" + cupcakeTop +
                ", cupcakeBottom=" + cupcakeBottom +
                ", quantity=" + quantity +
                '}';
    }
}