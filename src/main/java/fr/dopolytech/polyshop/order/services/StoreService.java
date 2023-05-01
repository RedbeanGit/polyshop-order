package fr.dopolytech.polyshop.order.services;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import fr.dopolytech.polyshop.order.domain.events.Event;

@Service
public class StoreService {
    private final QueueService queueService;

    public StoreService(QueueService queueService) {
        this.queueService = queueService;
    }

    public void send(Event command) {
        try {
            this.queueService.send(command, "order.updated");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
