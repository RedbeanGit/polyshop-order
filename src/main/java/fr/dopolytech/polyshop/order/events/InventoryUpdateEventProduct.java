package fr.dopolytech.polyshop.order.events;

public class InventoryUpdateEventProduct {
    public String productId;
    public Integer oldQuantity;
    public Integer newQuantity;
    public Integer changeRequested;
    public Boolean success;
}
