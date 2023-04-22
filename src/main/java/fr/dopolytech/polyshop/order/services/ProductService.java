package fr.dopolytech.polyshop.order.services;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import fr.dopolytech.polyshop.order.dtos.CreateProductDto;
import fr.dopolytech.polyshop.order.exceptions.CatalogApiUnreachableException;
import fr.dopolytech.polyshop.order.models.CatalogProduct;
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

    public Product createProduct(String orderId, CreateProductDto dto) throws Exception {
        String catalogUrl = "lb://catalog-service";
        WebClient webClient = webClientBuilder.build();
        CatalogProduct catalogProduct;

        try {
            catalogProduct = webClient.get().uri(catalogUrl + "/products/" + dto.productId)
                    .retrieve()
                    .bodyToMono(CatalogProduct.class).block();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CatalogApiUnreachableException("Can't reach catalog API");
        }

        Product product;

        if (catalogProduct == null) {
            product = new Product(dto.productId, "", 0.0, dto.quantity, orderId);
        } else {
            product = new Product(dto.productId, catalogProduct.name, catalogProduct.price, dto.quantity, orderId);
        }
        return productRepository.save(product);
    }
}
