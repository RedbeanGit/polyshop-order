package fr.dopolytech.polyshop.order.services;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class QueueService {
    private final RabbitTemplate rabbitTemplate;

    public QueueService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendProcess(String message) {
        rabbitTemplate.convertAndSend("orderExchange", "order.process", message);
    }
}
