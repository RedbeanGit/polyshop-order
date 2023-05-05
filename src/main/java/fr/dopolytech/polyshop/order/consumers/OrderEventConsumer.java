package fr.dopolytech.polyshop.order.consumers;

import org.springframework.stereotype.Component;

import com.eventstore.dbclient.EventStoreDBClient;
import com.eventstore.dbclient.ResolvedEvent;
import com.eventstore.dbclient.SubscribeToAllOptions;
import com.eventstore.dbclient.Subscription;
import com.eventstore.dbclient.SubscriptionListener;

import fr.dopolytech.polyshop.order.domain.events.EventType;
import fr.dopolytech.polyshop.order.domain.events.OrderCreatedEvent;
import fr.dopolytech.polyshop.order.domain.events.OrderUpdatedEvent;
import fr.dopolytech.polyshop.order.domain.events.ProductCreatedEvent;
import fr.dopolytech.polyshop.order.services.EventStoreService;
import fr.dopolytech.polyshop.order.services.OrderProjectorService;
import fr.dopolytech.polyshop.order.services.ProductProjectorService;

@Component
public class OrderEventConsumer extends SubscriptionListener {
    private final EventStoreDBClient client;
    private final EventStoreService eventStore;
    private final OrderProjectorService orderProjector;
    private final ProductProjectorService productProjector;

    public OrderEventConsumer(EventStoreDBClient client, EventStoreService eventStore,
            OrderProjectorService orderProjector, ProductProjectorService productProjector) {
        this.client = client;
        this.eventStore = eventStore;
        this.orderProjector = orderProjector;
        this.productProjector = productProjector;

        this.client.subscribeToAll(this, SubscribeToAllOptions.get().fromEnd());
    }

    public void onEvent(Subscription subscription, ResolvedEvent resolvedEvent) {
        try {
            EventType eventType = this.eventStore.getEventType(resolvedEvent);

            switch (eventType) {
                case ORDER_CREATED:
                    this.orderProjector
                            .createOrder(this.eventStore.deserialize(resolvedEvent, OrderCreatedEvent.class));
                    break;
                case ORDER_UPDATED:
                    this.orderProjector
                            .updateOrder(this.eventStore.deserialize(resolvedEvent, OrderUpdatedEvent.class));
                    break;
                case PRODUCT_CREATED:
                    this.productProjector
                            .createProduct(this.eventStore.deserialize(resolvedEvent, ProductCreatedEvent.class));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
