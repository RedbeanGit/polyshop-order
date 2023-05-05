package fr.dopolytech.polyshop.order.domain.events;

import fr.dopolytech.polyshop.order.domain.entities.OrderStatus;

public class OrderUpdatedEvent extends Event {
    public String orderId;
    public OrderStatus status;

    public OrderUpdatedEvent() {
        this.type = EventType.ORDER_UPDATED;
    }

    public OrderUpdatedEvent(String orderId, OrderStatus status) {
        this.orderId = orderId;
        this.status = status;
        this.type = EventType.ORDER_UPDATED;
    }
}
