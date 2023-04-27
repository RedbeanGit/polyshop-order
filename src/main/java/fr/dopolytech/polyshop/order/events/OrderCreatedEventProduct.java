package fr.dopolytech.polyshop.order.events;

public class OrderCreatedEventProduct {
    public String productId;
    public String name;
    public Integer quantity;
    public Double price;

    public OrderCreatedEventProduct(String productId, String name, int quantity, double price) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }
}
