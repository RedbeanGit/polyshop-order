package fr.dopolytech.polyshop.order.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import fr.dopolytech.polyshop.order.models.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
