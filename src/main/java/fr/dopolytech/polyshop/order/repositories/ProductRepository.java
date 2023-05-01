package fr.dopolytech.polyshop.order.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.dopolytech.polyshop.order.domain.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    public Iterable<Product> findByOrderId(String orderId);

    public Product findByOrderIdAndProductId(String orderId, String productId);
}
