package fr.dopolytech.polyshop.order.dtos;

public class CreateProductDto {
    public String productId;
    public int quantity;

    private long orderId;

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getOrderId() {
        return orderId;
    }
}
