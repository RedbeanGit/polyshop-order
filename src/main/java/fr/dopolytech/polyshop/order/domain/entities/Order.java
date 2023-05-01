package fr.dopolytech.polyshop.order.domain.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_placed")
public class Order {
    @Id
    @GeneratedValue
    public long id;

    @Column(name = "orderId", columnDefinition = "VARCHAR(255)")
    public String orderId;

    @Column(name = "date", columnDefinition = "TIMESTAMP")
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "UTC")
    public LocalDateTime date;

    @Enumerated(EnumType.ORDINAL)
    public OrderStatus status;

    public Order() {
    }

    public Order(String orderId, LocalDateTime date, OrderStatus status) {
        this.orderId = orderId;
        this.date = date;
        this.status = status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
