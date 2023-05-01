package fr.dopolytech.polyshop.order.domain.commands;

public class CreateProductCommand {
    public String productId;
    public String name;
    public Double price;
    public Integer quantity;

    public CreateProductCommand() {

    }

    public CreateProductCommand(String productId, String name, Double price, Integer quantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

}
