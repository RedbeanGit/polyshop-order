package fr.dopolytech.polyshop.order.events;

public class OrderCreatedEvent {
    public String orderId;
    public OrderCreatedEventProduct[] products;

    public OrderCreatedEvent(String orderId, OrderCreatedEventProduct[] products) {
        this.orderId = orderId;
        this.products = products;
    }
}
