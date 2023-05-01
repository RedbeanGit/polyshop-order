package fr.dopolytech.polyshop.order.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.dopolytech.polyshop.order.domain.entities.Order;
import fr.dopolytech.polyshop.order.domain.events.OrderCreatedEvent;
import fr.dopolytech.polyshop.order.domain.events.OrderUpdatedEvent;
import fr.dopolytech.polyshop.order.repositories.OrderRepository;

@Service
public class OrderProjectorService {
    private final OrderRepository repository;

    public OrderProjectorService(OrderRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void createOrder(OrderCreatedEvent event) {
        Order order = new Order(event.orderId, event.date, event.status);
        this.repository.save(order);
    }

    @Transactional
    public void updateOrder(OrderUpdatedEvent event) {
        Order order = this.repository.findByOrderId(event.orderId);
        order.setStatus(event.status);
        this.repository.save(order);
    }
}
