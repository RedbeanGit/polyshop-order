package fr.dopolytech.polyshop.order.services;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.dopolytech.polyshop.order.models.PolyshopEvent;

@Service
public class QueueService {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    public QueueService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    private void send(PolyshopEvent event, String exchange, String routingKey) throws JsonProcessingException {
        String message = this.stringify(event);
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

    public void sendCreated(PolyshopEvent event) throws JsonProcessingException {
        this.send(event, "orderExchange", "order.created");
    }

    public void sendChecked(PolyshopEvent event) throws JsonProcessingException {
        this.send(event, "orderExchange", "order.checked");
    }

    public void sendPaid(PolyshopEvent event) throws JsonProcessingException {
        this.send(event, "orderExchange", "order.paid");
    }

    public void sendShipped(PolyshopEvent event) throws JsonProcessingException {
        this.send(event, "orderExchange", "order.shipped");
    }

    public void sendCancelledCheck(PolyshopEvent event) throws JsonProcessingException {
        this.send(event, "orderExchange", "order.cancelled.check");
    }

    public void sendCancelledPayment(PolyshopEvent event) throws JsonProcessingException {
        this.send(event, "orderExchange", "order.cancelled.payment");
    }

    public void sendCancelledShipping(PolyshopEvent event) throws JsonProcessingException {
        this.send(event, "orderExchange", "order.cancelled.shipping");
    }

    public String stringify(PolyshopEvent data) throws JsonProcessingException {
        return mapper.writeValueAsString(data);
    }

    public PolyshopEvent parse(String data) throws JsonProcessingException {
        return mapper.readValue(data, PolyshopEvent.class);
    }
}
