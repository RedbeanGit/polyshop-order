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
    public long id;

    @Column(name = "product")
    public String productId;

    @Column(name = "name")
    public String name;

    @Column(name = "quantity")
    public int quantity;

    @Column(name = "price")
    public double price;

    @Column(name = "order_id")
    public String orderId;

    public Product() {

    }

    public Product(String productId, String name, double price, int quantity, String orderId) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.orderId = orderId;
    }

}
