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
        return new Queue("createOrderQueue", false);
    }

    @Bean
    public Queue checkSuccessOrderQueue() {
        return new Queue("checkSuccessOrderQueue", false);
    }

    @Bean
    public Queue checkFailedOrderQueue() {
        return new Queue("checkFailedOrderQueue", false);
    }

    @Bean
    public Queue paymentSuccessOrderQueue() {
        return new Queue("paymentSuccessOrderQueue", false);
    }

    @Bean
    public Queue paymentFailedOrderQueue() {
        return new Queue("paymentFailedOrderQueue", false);
    }

    @Bean
    public Queue shippingSuccessOrderQueue() {
        return new Queue("shippingSuccessOrderQueue", false);
    }

    @Bean
    public Queue shippingFailedOrderQueue() {
        return new Queue("shippingFailedOrderQueue", false);
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
    public Binding inventorySuccessBinding(Queue checkSuccessOrderQueue, TopicExchange inventoryExchange) {
        return BindingBuilder.bind(checkSuccessOrderQueue).to(inventoryExchange).with("inventory.update.success");
    }

    @Bean
    public Binding inventoryFailedBinding(Queue checkFailedOrderQueue, TopicExchange inventoryExchange) {
        return BindingBuilder.bind(checkFailedOrderQueue).to(inventoryExchange).with("inventory.update.failed");
    }

    @Bean
    public Binding paymentSuccessBinding(Queue paymentSuccessOrderQueue, TopicExchange paymentExchange) {
        return BindingBuilder.bind(paymentSuccessOrderQueue).to(paymentExchange).with("payment.done.success");
    }

    @Bean
    public Binding paymentFailedBinding(Queue paymentFailedOrderQueue, TopicExchange paymentExchange) {
        return BindingBuilder.bind(paymentFailedOrderQueue).to(paymentExchange).with("payment.done.failed");
    }

    @Bean
    public Binding shippingSuccessBinding(Queue shippingSuccessOrderQueue, TopicExchange shippingExchange) {
        return BindingBuilder.bind(shippingSuccessOrderQueue).to(shippingExchange).with("shipping.done.success");
    }

    @Bean
    public Binding shippingFailedBinding(Queue shippingFailedOrderQueue, TopicExchange shippingExchange) {
        return BindingBuilder.bind(shippingFailedOrderQueue).to(shippingExchange).with("shipping.done.failed");
    }
}
