package fr.dopolytech.polyshop.order.consumers.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import fr.dopolytech.polyshop.order.domain.entities.Order;
import fr.dopolytech.polyshop.order.domain.entities.Product;
import fr.dopolytech.polyshop.order.services.OrderService;
import fr.dopolytech.polyshop.order.services.ProductService;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final ProductService productService;
    private final OrderService orderService;

    public OrderController(OrderService orderService, ProductService productService) {
        this.orderService = orderService;
        this.productService = productService;
    }

    @GetMapping(produces = "application/json")
    public Iterable<Order> getAll() {
        return orderService.getOrders();
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public Order getOne(@PathVariable String id) {
        return orderService.getOrder(id);
    }

    @GetMapping(value = "/{id}/products", produces = "application/json")
    public Iterable<Product> findProducts(@PathVariable String id) {
        if (id == null) {
            throw new ResponseStatusException(BAD_REQUEST, "Order id is required");
        }
        return productService.getProductsByOrderId(id);
    }
}
