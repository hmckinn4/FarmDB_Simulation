package com.solvd.farm.binary;

import java.util.Objects;

public class Product extends BaseEntity{
    private String name;
    private String type;

    private int price;

    public Product(long id, String name, String type, int price) {
        super(id);
        this.name = name;
        this.type = type;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    //todo new equals method

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return super.getId() == super.getId() &&
                type == product.type &&
                Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, type, price);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + super.getId() +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", price=" + price +
                '}';
    }
}
