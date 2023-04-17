package fr.dopolytech.polyshop.order.events;

public class OrderEvent {
    public String orderId;
    public OrderEventProduct[] products;

    public OrderEvent(String orderId, OrderEventProduct[] products) {
        this.orderId = orderId;
        this.products = products;
    }
}
