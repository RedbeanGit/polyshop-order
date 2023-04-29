package fr.dopolytech.polyshop.order.models;

public class PolyshopEvent {
    public String orderId;
    public PolyshopEventProduct[] products;

    public PolyshopEvent() {
    }

    public PolyshopEvent(String orderId, PolyshopEventProduct[] products) {
        this.orderId = orderId;
        this.products = products;
    }
}
