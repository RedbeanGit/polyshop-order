package fr.dopolytech.polyshop.order.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import fr.dopolytech.polyshop.order.domain.entities.Order;
import fr.dopolytech.polyshop.order.domain.entities.OrderStatus;
import fr.dopolytech.polyshop.order.domain.events.OrderCreatedEvent;
import fr.dopolytech.polyshop.order.domain.events.OrderUpdatedEvent;
import fr.dopolytech.polyshop.order.repositories.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository repository;
    private final StoreService store;

    public OrderService(OrderRepository repository, StoreService store) {
        this.repository = repository;
        this.store = store;
    }

    public Iterable<Order> getOrders() {
        return this.repository.findAll();
    }

    public Order getOrder(String id) {
        return this.repository.findByOrderId(id);
    }

    public String createOrder() {
        String orderId = UUID.randomUUID().toString();
        OrderCreatedEvent event = new OrderCreatedEvent(orderId, LocalDateTime.now(),
                OrderStatus.CREATED);
        this.store.send(event);
        return orderId;
    }

    public void updateStatus(String orderId, OrderStatus status) {
        OrderUpdatedEvent event = new OrderUpdatedEvent(orderId, status);
        this.store.send(event);
    }
}
