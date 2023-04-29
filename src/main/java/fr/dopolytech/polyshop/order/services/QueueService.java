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

    public void sendOrderCreated(PolyshopEvent event) throws JsonProcessingException {
        String message = this.stringify(event);
        rabbitTemplate.convertAndSend("orderExchange", "order.created", message);
    }

    public void sendPaymentFailed(PolyshopEvent event) throws JsonProcessingException {
        String message = this.stringify(event);
        rabbitTemplate.convertAndSend("orderExchange", "order.cancelled.payment", message);
    }

    public void sendShippingFailed(PolyshopEvent event) throws JsonProcessingException {
        String message = this.stringify(event);
        rabbitTemplate.convertAndSend("orderExchange", "order.cancelled.shipping", message);
    }

    public String stringify(PolyshopEvent data) throws JsonProcessingException {
        return mapper.writeValueAsString(data);
    }

    public PolyshopEvent parse(String data) throws JsonProcessingException {
        return mapper.readValue(data, PolyshopEvent.class);
    }
}
