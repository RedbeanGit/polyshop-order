package fr.dopolytech.polyshop.order.domain.events;

import fr.dopolytech.polyshop.order.domain.entities.OrderStatus;

public class OrderUpdatedEvent extends Event {
    public EventType type = EventType.ORDER_UPDATED;

    public String orderId;
    public OrderStatus status;

    public OrderUpdatedEvent() {

    }

    public OrderUpdatedEvent(String orderId, OrderStatus status) {
        this.orderId = orderId;
        this.status = status;
    }
}
