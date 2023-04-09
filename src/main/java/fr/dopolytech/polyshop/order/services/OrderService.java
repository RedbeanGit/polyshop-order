package fr.dopolytech.polyshop.order.services;

import java.time.LocalDateTime;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import fr.dopolytech.polyshop.order.dtos.CreateOrderDto;
import fr.dopolytech.polyshop.order.exceptions.ValidationException;
import fr.dopolytech.polyshop.order.models.Order;
import fr.dopolytech.polyshop.order.repositories.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final QueueService queueService;

    public OrderService(OrderRepository orderRepository, QueueService queueService) {
        this.orderRepository = orderRepository;
        this.queueService = queueService;
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

    @RabbitListener(queues = "orderCheckoutQueue")
    public void processOrder(String message) {
        System.out.println("Message received: " + message);
        queueService.sendProcess(message);
    }
}
