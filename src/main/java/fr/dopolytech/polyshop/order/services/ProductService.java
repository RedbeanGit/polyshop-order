package fr.dopolytech.polyshop.order.services;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import fr.dopolytech.polyshop.order.dtos.CreateProductDto;
import fr.dopolytech.polyshop.order.exceptions.CatalogApiUnreachableException;
import fr.dopolytech.polyshop.order.exceptions.NotFoundException;
import fr.dopolytech.polyshop.order.exceptions.ValidationException;
import fr.dopolytech.polyshop.order.models.CatalogProduct;
import fr.dopolytech.polyshop.order.models.InventoryProduct;
import fr.dopolytech.polyshop.order.models.Product;
import fr.dopolytech.polyshop.order.repositories.ProductRepository;
import reactor.core.publisher.Mono;

@Service
public class ProductService {
    @LoadBalanced
    private final WebClient.Builder webClientBuilder;
    private final ProductRepository productRepository;

    public ProductService(WebClient.Builder webClientBuilder, ProductRepository productRepository) {
        this.webClientBuilder = webClientBuilder;
        this.productRepository = productRepository;
    }

    public void validateCreateDto(CreateProductDto productDto) throws ValidationException {
        if (productDto.productId == null || productDto.productId.isBlank()) {
            throw new ValidationException("Product id is required");
        }

        if (productDto.quantity <= 0) {
            throw new ValidationException("Quantity must be greater than 0");
        }
    }

    public Product createDtoToProduct(CreateProductDto productDto) throws Exception {
        String catalogUrl = "lb://catalog-service";
        String inventoryUrl = "lb://inventory-service";

        Mono<InventoryProduct> reactiveInventoryProduct;
        Mono<CatalogProduct> reactiveCatalogProduct;

        WebClient webClient = webClientBuilder.build();

        try {
            reactiveInventoryProduct = webClient.get().uri(inventoryUrl + "/products/" +
                    productDto.productId).retrieve()
                    .bodyToMono(InventoryProduct.class);
        } catch (Exception e) {
            System.out.println(e);
            throw new CatalogApiUnreachableException("Can't reach catalog API");
        }

        try {
            reactiveCatalogProduct = webClient.get().uri(catalogUrl + "/products?inventory=" +
                    productDto.productId)
                    .retrieve().bodyToFlux(CatalogProduct.class).next();
        } catch (Exception e) {
            throw new CatalogApiUnreachableException("Can't reach inventory API");
        }

        InventoryProduct inventoryProduct = reactiveInventoryProduct.block();
        CatalogProduct catalogProduct = reactiveCatalogProduct.block();

        if (inventoryProduct == null) {
            throw new NotFoundException("Product not found in inventory");
        }

        if (catalogProduct == null) {
            throw new NotFoundException("Product not found in catalog");
        }

        return new Product(catalogProduct.name, inventoryProduct.price, inventoryProduct.quantity,
                productDto.getOrderId());
    }

    public Iterable<Product> getAll() {
        return productRepository.findAll();
    }

    public Iterable<Product> getAllByOrderId(Long orderId) {
        return productRepository.findAllByOrderId(orderId);
    }

    public Product getOne(Long id) {
        return productRepository.findById(id).get();
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }
}
