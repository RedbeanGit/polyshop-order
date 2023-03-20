package fr.dopolytech.polyshop.order.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private double price;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "order_id")
    private long orderId;

    public Product() {
        
    }

    public Product(String name, double price, int quantity, long orderId) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.orderId = orderId;
    }

    public long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public long getOrderId() {
        return orderId;
    }
}
