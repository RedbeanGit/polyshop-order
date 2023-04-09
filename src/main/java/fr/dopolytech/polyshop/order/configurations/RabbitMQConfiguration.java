package fr.dopolytech.polyshop.order.configurations;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {
    @Bean
    public Queue orderQueue() {
        return new Queue("orderCheckoutQueue", true);
    }

    @Bean
    public TopicExchange cartExchange() {
        return new TopicExchange("cartExchange");
    }

    @Bean
    public Binding orderBinding(Queue orderQueue, TopicExchange cartExchange) {
        return BindingBuilder.bind(orderQueue).to(cartExchange).with("cart.checkout");
    }
}
