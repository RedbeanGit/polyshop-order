package fr.dopolytech.polyshop.order.services;

import org.springframework.stereotype.Service;

import fr.dopolytech.polyshop.order.domain.entities.Product;
import fr.dopolytech.polyshop.order.domain.events.ProductCreatedEvent;
import fr.dopolytech.polyshop.order.repositories.ProductRepository;

@Service
public class ProductProjectorService {
    private final ProductRepository repository;

    public ProductProjectorService(ProductRepository repository) {
        this.repository = repository;
    }

    public void createProduct(ProductCreatedEvent event) {
        Product product = new Product(event.productId, event.name, event.price, event.quantity, event.orderId);
        this.repository.save(product);
    }
}
