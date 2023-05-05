package fr.dopolytech.polyshop.order.domain.events;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import fr.dopolytech.polyshop.order.domain.entities.OrderStatus;

public class OrderCreatedEvent extends Event {
    public String orderId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "UTC")
    public LocalDateTime date;

    public OrderStatus status;

    public OrderCreatedEvent() {
        this.type = EventType.ORDER_CREATED;
    }

    public OrderCreatedEvent(String orderId, LocalDateTime date, OrderStatus status) {
        this.orderId = orderId;
        this.date = date;
        this.status = status;
        this.type = EventType.ORDER_CREATED;
    }
}
