package fr.dopolytech.polyshop.order.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_placed")
public class Order {
    @Id
    @GeneratedValue
    public long id;

    @Column(name = "date", columnDefinition = "TIMESTAMP")
    public LocalDateTime date;

    public Order() {}

    public Order(LocalDateTime date) {
        System.out.println("Order created at " + date);
        this.date = date;
    }
}
