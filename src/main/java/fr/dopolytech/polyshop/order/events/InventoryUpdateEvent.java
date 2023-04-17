package fr.dopolytech.polyshop.order.events;

public class InventoryUpdateEvent {
    public String orderId;
    public InventoryUpdateEventProduct[] products;
}
