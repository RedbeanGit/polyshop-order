package fr.dopolytech.polyshop.order.dtos;

public class CreateProductDto {
    public String productId;
    public Integer quantity;

    public CreateProductDto() {

    }

    public CreateProductDto(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

}
