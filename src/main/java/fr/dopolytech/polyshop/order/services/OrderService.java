package fr.dopolytech.polyshop.order.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import fr.dopolytech.polyshop.order.dtos.CreateOrderDto;
import fr.dopolytech.polyshop.order.exceptions.ValidationException;
import fr.dopolytech.polyshop.order.models.Order;
import fr.dopolytech.polyshop.order.repositories.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createDtoToOrder(CreateOrderDto dto) {
        return new Order(LocalDateTime.now());
    }

    public void validateCreateDto(CreateOrderDto dto) throws ValidationException {
    }

    public Iterable<Order> getAll() {
        return orderRepository.findAll();
    }

    public Order getOne(Long id) {
        return orderRepository.findById(id).get();
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }
}
