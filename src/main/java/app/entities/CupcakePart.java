package app.entities;

import java.util.Objects;

public class CupcakePart {
    public enum Type {
        TOP,
        BOTTOM
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CupcakePart that = (CupcakePart) o;
        return getCupcakePartId() == that.getCupcakePartId() && getPrice() == that.getPrice() && Objects.equals(getName(), that.getName()) && getType() == that.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCupcakePartId(), getName(), getPrice(), getType());
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
