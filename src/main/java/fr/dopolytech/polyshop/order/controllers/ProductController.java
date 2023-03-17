package fr.dopolytech.polyshop.order.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import fr.dopolytech.polyshop.order.dtos.CreateProductDto;
import fr.dopolytech.polyshop.order.dtos.exceptions.CatalogApiUnreachableException;
import fr.dopolytech.polyshop.order.dtos.exceptions.DtoException;
import fr.dopolytech.polyshop.order.dtos.exceptions.NotFoundException;
import fr.dopolytech.polyshop.order.dtos.exceptions.ValidationException;
import fr.dopolytech.polyshop.order.models.Product;
import fr.dopolytech.polyshop.order.repositories.ProductRepository;

@RestController
@RequestMapping("/products")
class ProductController {
    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public Product addProductToOrder(@RequestBody CreateProductDto dto) throws DtoException {
        try {
            dto.validate();
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
