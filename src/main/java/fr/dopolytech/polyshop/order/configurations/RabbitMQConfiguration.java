package fr.dopolytech.polyshop.order.configurations;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {
    // Queues

    @Bean
    public Queue createOrderQueue() {
        return new Queue("createOrderQueue", true);
    }

    @Bean
    public Queue checkOrderQueue() {
        return new Queue("checkOrderQueue", true);
    }

    @Bean
    public Queue payOrderQueue() {
        return new Queue("payOrderQueue", true);
    }

    @Bean
    public Queue shipOrderQueue() {
        return new Queue("shipOrderQueue", true);
    }

    // Exchanges

    @Bean
    public TopicExchange cartExchange() {
        return new TopicExchange("cartExchange");
    }

    @Bean
    public TopicExchange inventoryExchange() {
        return new TopicExchange("inventoryExchange");
    }

    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange("paymentExchange");
    }

    @Bean
    public TopicExchange shippingExchange() {
        return new TopicExchange("shippingExchange");
    }

    // Bindings

    @Bean
    public Binding orderBinding(Queue createOrderQueue, TopicExchange cartExchange) {
        return BindingBuilder.bind(createOrderQueue).to(cartExchange).with("cart.checkout");
    }

    @Bean
    public Binding inventoryBinding(Queue checkOrderQueue, TopicExchange inventoryExchange) {
        return BindingBuilder.bind(checkOrderQueue).to(inventoryExchange).with("inventory.update");
    }

    @Bean
    public Binding paymentBinding(Queue payOrderQueue, TopicExchange paymentExchange) {
        return BindingBuilder.bind(payOrderQueue).to(paymentExchange).with("payment.done");
    }

    @Bean
    public Binding shippingBinding(Queue shipOrderQueue, TopicExchange shippingExchange) {
        return BindingBuilder.bind(shipOrderQueue).to(shippingExchange).with("shipping.done");
    }
}
