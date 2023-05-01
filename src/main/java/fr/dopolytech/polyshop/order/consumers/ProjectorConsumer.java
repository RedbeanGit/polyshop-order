package fr.dopolytech.polyshop.order.consumers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import fr.dopolytech.polyshop.order.domain.events.Event;
import fr.dopolytech.polyshop.order.domain.events.OrderCreatedEvent;
import fr.dopolytech.polyshop.order.domain.events.OrderUpdatedEvent;
import fr.dopolytech.polyshop.order.domain.events.ProductCreatedEvent;
import fr.dopolytech.polyshop.order.services.OrderProjectorService;
import fr.dopolytech.polyshop.order.services.ProductProjectorService;
import fr.dopolytech.polyshop.order.services.QueueService;

@Component
public class ProjectorConsumer {
    private final QueueService queueService;
    private final OrderProjectorService orderProjector;
    private final ProductProjectorService productProjector;

    public ProjectorConsumer(QueueService queueService, OrderProjectorService orderProjector,
            ProductProjectorService productProjector) {
        this.queueService = queueService;
        this.orderProjector = orderProjector;
        this.productProjector = productProjector;
    }

    @RabbitListener(queues = "orderEventQueue")
    public void onEvent(String message) {
        try {
            Event event = this.queueService.parse(message, Event.class);

            switch (event.type) {
                case ORDER_CREATED:
                    this.orderProjector.createOrder(this.queueService.parse(message, OrderCreatedEvent.class));
                    break;
                case ORDER_UPDATED:
                    this.orderProjector.updateOrder(this.queueService.parse(message, OrderUpdatedEvent.class));
                    break;
                case PRODUCT_CREATED:
                    this.productProjector.createProduct(this.queueService.parse(message, ProductCreatedEvent.class));
                    break;
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
