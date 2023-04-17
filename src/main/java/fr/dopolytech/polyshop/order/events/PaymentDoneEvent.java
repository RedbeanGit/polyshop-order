package fr.dopolytech.polyshop.order.events;

public class PaymentDoneEvent {
    public String orderId;
    public String price;
    public Boolean success;
}
