package fr.dopolytech.polyshop.order.services;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import fr.dopolytech.polyshop.order.dtos.CreateProductDto;
import fr.dopolytech.polyshop.order.models.Product;
import fr.dopolytech.polyshop.order.repositories.ProductRepository;

@Service
public class ProductService {
    @LoadBalanced
    private final WebClient.Builder webClientBuilder;
    private final ProductRepository productRepository;

    public ProductService(WebClient.Builder webClientBuilder, ProductRepository productRepository) {
        this.webClientBuilder = webClientBuilder;
        this.productRepository = productRepository;
    }

    public Product getProduct(String orderId, String productId) {
        return productRepository.findByOrderIdAndProductId(orderId, productId);
    }

    public Iterable<Product> getProductsByOrderId(String orderId) {
        return productRepository.findByOrderId(orderId);
    }

    public Product createProduct(String orderId, CreateProductDto dto) {
        Product product = new Product(dto.productId, dto.name, dto.price, dto.quantity, orderId);
        return productRepository.save(product);
    }
}
