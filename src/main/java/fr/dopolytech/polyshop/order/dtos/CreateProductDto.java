package fr.dopolytech.polyshop.order.dtos;

import org.springframework.web.client.RestTemplate;

import fr.dopolytech.polyshop.order.dtos.exceptions.CatalogApiUnreachableException;
import fr.dopolytech.polyshop.order.dtos.exceptions.DtoException;
import fr.dopolytech.polyshop.order.dtos.exceptions.NotFoundException;
import fr.dopolytech.polyshop.order.dtos.exceptions.ValidationException;
import fr.dopolytech.polyshop.order.models.CatalogProduct;
import fr.dopolytech.polyshop.order.models.Product;

public class CreateProductDto implements Dto<Product> {
    public String productId;
    public int quantity;
    public long orderId;

    @Override
    public void validate() throws DtoException {
        if (productId == null || productId.isBlank()) {
            throw new ValidationException("Product id is required");
        }

        if (quantity <= 0) {
            throw new ValidationException("Quantity must be greater than 0");
        }
    }
    @Override
    public Product toModel() throws DtoException {
        String catalogUrl = System.getenv("CATALOG_API_URL");
        RestTemplate restTemplate = new RestTemplate();
        CatalogProduct catalogProduct;

        try {
            catalogProduct = restTemplate.getForObject(catalogUrl + "/products/" + productId, CatalogProduct.class);
        } catch (Exception e) {
            throw new CatalogApiUnreachableException("Can't reach catalog API");
        }

        if (catalogProduct == null) {
            throw new NotFoundException("Product not found");
        }
        
        return new Product(catalogProduct.getName(), catalogProduct.getPrice(), quantity, orderId);
    }
    @Override
    public Dto<Product> fromModel(Product model) {
        this.orderId = model.getOrderId();
        return this;
    }
}
