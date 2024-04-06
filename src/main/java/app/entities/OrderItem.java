package app.entities;

public class OrderItem {
    private int orderItemId;
    private CupcakePart cupcakeTop;
    private CupcakePart cupcakeBottom;
    private int quantity;

    public OrderItem(int orderItemId, CupcakePart cupcakeTop, CupcakePart cupcakeBottom, int quantity) {
        this.orderItemId = orderItemId;
        this.cupcakeTop = cupcakeTop;
        this.cupcakeBottom = cupcakeBottom;
        this.quantity = quantity;
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
