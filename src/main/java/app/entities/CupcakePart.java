package app.entities;

public class CupcakePart {
    enum Type {
        TOP,
        BOTTOM,
    }

    private int cupcakePartId;
    private String name;
    private int price;
    private Type type;

    public CupcakePart(int cupcakePartId, String name, int price, Type type) {
        this.cupcakePartId = cupcakePartId;
        this.name = name;
        this.price = price;
        this.type = type;
    }

    public int getCupcakePartId() {
        return cupcakePartId;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "CupcakePart{" +
                "cupcakePartId=" + cupcakePartId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", type=" + type +
                '}';
    }
}
