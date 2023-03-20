package fr.dopolytech.polyshop.order.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.dopolytech.polyshop.order.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    public Iterable<Product> findAllByOrderId(long orderId);
}
