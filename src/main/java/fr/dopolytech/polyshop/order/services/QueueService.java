package fr.dopolytech.polyshop.order.services;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class QueueService {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper mapper;

    public QueueService(RabbitTemplate rabbitTemplate, ObjectMapper mapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.mapper = mapper;
    }

    public <T> void send(T payload, String routingKey) throws JsonProcessingException {
        String message = this.stringify(payload);
        rabbitTemplate.convertAndSend("orderExchange", routingKey, message);
    }

    public <T> String stringify(T data) throws JsonProcessingException {
        return mapper.writeValueAsString(data);
    }

    public <T> T parse(String data, Class<T> cls) throws JsonProcessingException {
        return mapper.readValue(data, cls);
    }
}
