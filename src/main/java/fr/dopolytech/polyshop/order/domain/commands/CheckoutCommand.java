package fr.dopolytech.polyshop.order.domain.commands;

public class CheckoutCommand {
    public String orderId;
    public CheckoutCommandProduct[] products;

    public CheckoutCommand() {
    }

    public CheckoutCommand(String orderId, CheckoutCommandProduct[] products) {
        this.orderId = orderId;
        this.products = products;
    }
}
