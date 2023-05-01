package fr.dopolytech.polyshop.order.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.dopolytech.polyshop.order.domain.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    public Order findByOrderId(String orderId);
}
