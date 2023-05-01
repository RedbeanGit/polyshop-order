package fr.dopolytech.polyshop.order.domain.events;

public class ProductCreatedEvent extends Event {
    public EventType type = EventType.PRODUCT_CREATED;

    public String productId;
    public String name;
    public Double price;
    public Integer quantity;
    public String orderId;

    public ProductCreatedEvent() {

    }

    public ProductCreatedEvent(String productId, String name, Double price, Integer quantity, String orderId) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.orderId = orderId;
    }
}
