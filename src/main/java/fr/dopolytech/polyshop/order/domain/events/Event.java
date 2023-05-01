package fr.dopolytech.polyshop.order.domain.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
    public EventType type;
}
