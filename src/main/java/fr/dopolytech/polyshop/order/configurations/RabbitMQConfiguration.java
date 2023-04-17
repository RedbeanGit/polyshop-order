package fr.dopolytech.polyshop.order.configurations;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {
    // Queues

    @Bean
    public Queue cartCheckoutQueue() {
        return new Queue("cartCheckoutQueue", true);
    }

    @Bean
    public Queue inventoryUpdateQueue() {
        return new Queue("inventoryUpdateQueue", true);
    }

    @Bean
    public Queue paymentDoneQueue() {
        return new Queue("paymentDoneQueue", true);
    }

    @Bean
    public Queue shippingDoneQueue() {
        return new Queue("shippingDoneQueue", true);
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
    public Binding orderBinding(@Qualifier("cartCheckoutQueue") Queue queue,
            @Qualifier("cartExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("cart.checkout");
    }

    @Bean
    public Binding inventoryBinding(@Qualifier("inventoryUpdateQueue") Queue queue,
            @Qualifier("inventoryExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("inventory.update");
    }

    @Bean
    public Binding paymentBinding(@Qualifier("paymentDoneQueue") Queue queue,
            @Qualifier("paymentExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("payment.done");
    }

    @Bean
    public Binding shippingBinding(@Qualifier("shippingDoneQueue") Queue queue,
            @Qualifier("shippingExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("shipping.done");
    }
}
