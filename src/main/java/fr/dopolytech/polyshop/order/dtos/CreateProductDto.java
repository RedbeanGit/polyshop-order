package fr.dopolytech.polyshop.order.dtos;

public class CreateProductDto {
    public String productId;
    public String name;
    public Double price;
    public Integer quantity;

    public CreateProductDto() {

    }

    public CreateProductDto(String productId, String name, Double price, Integer quantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

}
