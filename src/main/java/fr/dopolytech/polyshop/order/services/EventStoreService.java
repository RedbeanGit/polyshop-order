package fr.dopolytech.polyshop.order.services;

import org.springframework.stereotype.Service;

import com.eventstore.dbclient.EventData;
import com.eventstore.dbclient.EventDataBuilder;
import com.eventstore.dbclient.EventStoreDBClient;
import com.eventstore.dbclient.ResolvedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.dopolytech.polyshop.order.domain.events.Event;
import fr.dopolytech.polyshop.order.domain.events.EventType;

@Service
public class EventStoreService {
    private final EventStoreDBClient client;
    private final ObjectMapper mapper;

    public EventStoreService(EventStoreDBClient client, ObjectMapper mapper) {
        this.client = client;
        this.mapper = mapper;
    }

    public <T extends Event> void send(String streamName, T event) throws Exception {
        this.client.appendToStream(streamName, this.serialize(event)).get();
    }

    public <T extends Event> EventData serialize(T event) throws JsonProcessingException {
        return EventDataBuilder.binary(event.type.name(), this.mapper.writeValueAsBytes(event)).build();
    }

    public <T extends Event> T deserialize(ResolvedEvent event, Class<T> type) throws Exception {
        return this.mapper.readValue(event.getEvent().getEventData(), type);
    }

    public EventType getEventType(ResolvedEvent event) {
        return EventType.valueOf(event.getEvent().getEventType());
    }
}
