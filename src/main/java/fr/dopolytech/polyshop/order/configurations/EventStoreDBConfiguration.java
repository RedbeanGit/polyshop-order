package fr.dopolytech.polyshop.order.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eventstore.dbclient.ConnectionStringParsingException;
import com.eventstore.dbclient.EventStoreDBClient;
import com.eventstore.dbclient.EventStoreDBClientSettings;
import com.eventstore.dbclient.EventStoreDBConnectionString;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class EventStoreDBConfiguration {
    @Bean
    public EventStoreDBClient client(@Value("${eventstoredb.connection-string}") String connectionString,
            ObjectMapper objectMapper)
            throws ConnectionStringParsingException {
        EventStoreDBClientSettings settings = EventStoreDBConnectionString.parse(connectionString);
        return EventStoreDBClient.create(settings);
    }
}
