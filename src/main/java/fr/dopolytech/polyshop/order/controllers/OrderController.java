package fr.dopolytech.polyshop.order.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import fr.dopolytech.polyshop.order.dtos.CreateOrderDto;
import fr.dopolytech.polyshop.order.dtos.exceptions.DtoException;
import fr.dopolytech.polyshop.order.dtos.exceptions.ValidationException;
import fr.dopolytech.polyshop.order.models.Order;
import fr.dopolytech.polyshop.order.repositories.OrderRepository;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping(produces = "application/json")
    public Iterable<Order> findAll() {
        return orderRepository.findAll();
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public Order findById(Long id) {
        return orderRepository.findById(id).get();
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public Order create(@RequestBody CreateOrderDto dto) throws DtoException {
        try {
            dto.validate();
            return orderRepository.save(dto.toModel());
        } catch (ValidationException e) {
            throw new ResponseStatusException(BAD_REQUEST, e.getMessage());
        }
    }
}
