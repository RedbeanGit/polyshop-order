package fr.dopolytech.polyshop.order.domain.entities;

public enum OrderStatus {
    CREATED,
    CHECKED,
    CHECK_FAILED,
    PAID,
    PAYMENT_FAILED,
    SHIPPED,
    SHIPPING_FAILED,
}
