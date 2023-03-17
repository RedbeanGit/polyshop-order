package fr.dopolytech.polyshop.order.dtos;

import java.time.LocalDateTime;

import fr.dopolytech.polyshop.order.dtos.exceptions.DtoException;
import fr.dopolytech.polyshop.order.models.Order;

public class CreateOrderDto implements Dto<Order> {

    @Override
    public void validate() throws DtoException {}

    @Override
    public Order toModel() {
        return new Order(LocalDateTime.now());
    }

    @Override
    public Dto<Order> fromModel(Order model) {
        return this;
    }
}
