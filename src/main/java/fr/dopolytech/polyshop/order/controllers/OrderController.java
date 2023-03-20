package fr.dopolytech.polyshop.order.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.BAD_GATEWAY;

import fr.dopolytech.polyshop.order.dtos.CreateOrderDto;
import fr.dopolytech.polyshop.order.dtos.CreateProductDto;
import fr.dopolytech.polyshop.order.dtos.exceptions.CatalogApiUnreachableException;
import fr.dopolytech.polyshop.order.dtos.exceptions.DtoException;
import fr.dopolytech.polyshop.order.dtos.exceptions.NotFoundException;
import fr.dopolytech.polyshop.order.dtos.exceptions.ValidationException;
import fr.dopolytech.polyshop.order.models.Order;
import fr.dopolytech.polyshop.order.models.Product;
import fr.dopolytech.polyshop.order.repositories.OrderRepository;
import fr.dopolytech.polyshop.order.repositories.ProductRepository;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderController(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @GetMapping(produces = "application/json")
    public Iterable<Order> findAll() {
        return orderRepository.findAll();
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public Order findById(@PathVariable Long id) {
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

    @GetMapping(value = "/{id}/products", produces = "application/json")
    public Iterable<Product> getProducts(@PathVariable Long id) {
        if (id == null) {
            throw new ResponseStatusException(BAD_REQUEST, "Order id is required");
        }
        return productRepository.findAllByOrderId(id);
    }

    @PostMapping(value = "/{id}/products", consumes = "application/json", produces = "application/json")
    public Product addProductToOrder(@PathVariable Long id, @RequestBody CreateProductDto dto) throws DtoException {
        if (id == null) {
            throw new ResponseStatusException(BAD_REQUEST, "Order id is required");
        }
        try {
            dto.validate();
            dto.setOrderId(id);
            return productRepository.save(dto.toModel());
        } catch (ValidationException e) {
            throw new ResponseStatusException(BAD_REQUEST, e.getMessage());
        } catch (CatalogApiUnreachableException e) {
            throw new ResponseStatusException(BAD_GATEWAY, e.getMessage());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(NOT_FOUND, e.getMessage());
        }
    }
}
