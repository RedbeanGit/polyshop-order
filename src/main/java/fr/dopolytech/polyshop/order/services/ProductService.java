package fr.dopolytech.polyshop.order.services;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import fr.dopolytech.polyshop.order.domain.commands.CreateProductCommand;
import fr.dopolytech.polyshop.order.domain.entities.Product;
import fr.dopolytech.polyshop.order.domain.events.ProductCreatedEvent;
import fr.dopolytech.polyshop.order.repositories.ProductRepository;

@Service
public class ProductService {
    @LoadBalanced
    private final WebClient.Builder webClientBuilder;
    private final ProductRepository repository;
    private final StoreService store;

    public ProductService(WebClient.Builder webClientBuilder, ProductRepository repository, StoreService store) {
        this.webClientBuilder = webClientBuilder;
        this.repository = repository;
        this.store = store;
    }

    public Product getProduct(String orderId, String productId) {
        return repository.findByOrderIdAndProductId(orderId, productId);
    }

    public Iterable<Product> getProductsByOrderId(String orderId) {
        return repository.findByOrderId(orderId);
    }

    public void createProduct(String orderId, CreateProductCommand command) {
        ProductCreatedEvent event = new ProductCreatedEvent(command.productId, command.name, command.price,
                command.quantity, orderId);
        this.store.send(event);
    }
}
