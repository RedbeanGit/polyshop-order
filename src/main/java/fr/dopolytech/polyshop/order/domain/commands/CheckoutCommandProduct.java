package fr.dopolytech.polyshop.order.domain.commands;

public class CheckoutCommandProduct {
    public String id;
    public String name;
    public Double price;
    public Integer quantity;

    public CheckoutCommandProduct() {
    }

    public CheckoutCommandProduct(String id, String name, Double price, Integer quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}
