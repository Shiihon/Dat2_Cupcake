package app.entities;

public class OrderItem {
    private int orderItemId;
    private CupcakePart cupcakeTop;
    private CupcakePart cupcakeBottom;
    private int quantity;
    private int totalPrice;

    public OrderItem(int orderItemId, CupcakePart cupcakeTop, CupcakePart cupcakeBottom, int quantity, int totalPrice) {
        this.orderItemId = orderItemId;
        this.cupcakeTop = cupcakeTop;
        this.cupcakeBottom = cupcakeBottom;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public int getOrderItemId() {
        return orderItemId;
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

    public int getTotalPrice() {
        return totalPrice;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "orderItemId=" + orderItemId +
                ", cupcakeTop=" + cupcakeTop +
                ", cupcakeBottom=" + cupcakeBottom +
                ", quantity=" + quantity +
                '}';
    }
}
