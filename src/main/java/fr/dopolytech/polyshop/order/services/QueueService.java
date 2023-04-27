package fr.dopolytech.polyshop.order.services;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.dopolytech.polyshop.order.events.OrderCreatedEvent;

@Service
public class QueueService {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    public QueueService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOrderCreated(OrderCreatedEvent event) throws JsonProcessingException {
        String message = this.stringify(event);
        rabbitTemplate.convertAndSend("orderExchange", "order.created", message);
    }

    public String stringify(Object data) throws JsonProcessingException {
        return mapper.writeValueAsString(data);
    }

    public <T> T parse(String data, Class<T> type) throws JsonProcessingException {
        return mapper.readValue(data, type);
    }
}
