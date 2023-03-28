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
import fr.dopolytech.polyshop.order.exceptions.CatalogApiUnreachableException;
import fr.dopolytech.polyshop.order.exceptions.NotFoundException;
import fr.dopolytech.polyshop.order.exceptions.ValidationException;
import fr.dopolytech.polyshop.order.models.Order;
import fr.dopolytech.polyshop.order.models.Product;
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
    public Iterable<Order> findAll() {
        return orderService.getAll();
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public Order findById(@PathVariable Long id) {
        return orderService.getOne(id);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public Order create(@RequestBody CreateOrderDto dto) throws Exception {
        try {
            orderService.validateCreateDto(dto);
            return orderService.save(orderService.createDtoToOrder(dto));
        } catch (ValidationException e) {
            throw new ResponseStatusException(BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping(value = "/{id}/products", produces = "application/json")
    public Iterable<Product> getProducts(@PathVariable Long id) {
        if (id == null) {
            throw new ResponseStatusException(BAD_REQUEST, "Order id is required");
        }
        return productService.getAllByOrderId(id);
    }

    @PostMapping(value = "/{id}/products", consumes = "application/json", produces = "application/json")
    public Product addProductToOrder(@PathVariable Long id, @RequestBody CreateProductDto dto) throws Exception {
        if (id == null) {
            throw new ResponseStatusException(BAD_REQUEST, "Order id is required");
        }
        try {
            dto.setOrderId(id);
            productService.validateCreateDto(dto);
            return productService.save(productService.createDtoToProduct(dto));
        } catch (ValidationException e) {
            throw new ResponseStatusException(BAD_REQUEST, e.getMessage());
        } catch (CatalogApiUnreachableException e) {
            throw new ResponseStatusException(BAD_GATEWAY, e.getMessage());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(NOT_FOUND, e.getMessage());
        }
    }
}
