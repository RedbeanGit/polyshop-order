package fr.dopolytech.polyshop.order.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import fr.dopolytech.polyshop.order.dtos.CreateProductDto;
import fr.dopolytech.polyshop.order.events.CartCheckoutEvent;
import fr.dopolytech.polyshop.order.events.CartCheckoutEventProduct;
import fr.dopolytech.polyshop.order.events.InventoryUpdateEvent;
import fr.dopolytech.polyshop.order.events.InventoryUpdateEventProduct;
import fr.dopolytech.polyshop.order.events.OrderCreatedEvent;
import fr.dopolytech.polyshop.order.events.OrderCreatedEventProduct;
import fr.dopolytech.polyshop.order.events.PaymentDoneEvent;
import fr.dopolytech.polyshop.order.events.ShippingDoneEvent;
import fr.dopolytech.polyshop.order.models.Order;
import fr.dopolytech.polyshop.order.models.OrderStatus;
import fr.dopolytech.polyshop.order.models.Product;
import fr.dopolytech.polyshop.order.repositories.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final QueueService queueService;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepository, QueueService queueService, ProductService productService) {
        this.orderRepository = orderRepository;
        this.queueService = queueService;
        this.productService = productService;
    }

    public Iterable<Order> getOrders() {
        return this.orderRepository.findAll();
    }

    public Order getOrder(String id) {
        return this.orderRepository.findByOrderId(id);
    }

    public Order createOrder() {
        Order order = new Order(UUID.randomUUID().toString(), LocalDateTime.now(), OrderStatus.CREATED);
        return this.orderRepository.save(order);
    }

    public Order updateStatus(String orderId, OrderStatus status) {
        Order order = this.getOrder(orderId);
        order.status = status;
        return this.orderRepository.save(order);
    }

    @RabbitListener(queues = "createOrderQueue")
    public void onCartCheckout(String message) {
        try {
            CartCheckoutEvent cartCheckoutEvent = this.queueService.parse(message, CartCheckoutEvent.class);
            List<OrderCreatedEventProduct> orderEventProducts = new ArrayList<OrderCreatedEventProduct>();

            Order order = this.createOrder();

            for (CartCheckoutEventProduct cartEventProduct : cartCheckoutEvent.products) {
                CreateProductDto productDto = new CreateProductDto(cartEventProduct.productId,
                        cartEventProduct.quantity);

                try {
                    Product product = this.productService.createProduct(order.orderId, productDto);
                    OrderCreatedEventProduct orderEventProduct = new OrderCreatedEventProduct(product.productId,
                            product.name, product.quantity, product.price);
                    orderEventProducts.add(orderEventProduct);
                } catch (Exception e) {
                    e.printStackTrace();
                    this.updateStatus(order.orderId, OrderStatus.CANCELLED);
                    return;
                }
            }

            OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(order.orderId,
                    orderEventProducts.toArray(new OrderCreatedEventProduct[orderEventProducts.size()]));
            this.queueService.sendOrderCreated(orderCreatedEvent);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "checkOrderQueue")
    public void onInventoryUpdate(String message) {
        try {
            InventoryUpdateEvent inventoryUpdateEvent = this.queueService.parse(message, InventoryUpdateEvent.class);

            if (inventoryUpdateEvent.orderId != null) {
                if (Arrays.asList(inventoryUpdateEvent.products).stream().map(product -> product.success)
                        .anyMatch(success -> !success)) {
                    this.updateStatus(inventoryUpdateEvent.orderId, OrderStatus.CANCELLED);
                } else {
                    this.updateStatus(inventoryUpdateEvent.orderId, OrderStatus.POSSIBLE);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "payOrderQueue")
    public void onPaymentDone(String message) {
        try {
            PaymentDoneEvent paymentDoneEvent = this.queueService.parse(message, PaymentDoneEvent.class);

            if (paymentDoneEvent.success) {
                this.updateStatus(paymentDoneEvent.orderId, OrderStatus.PAID);
            } else {
                this.updateStatus(paymentDoneEvent.orderId, OrderStatus.CANCELLED);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "shipOrderQueue")
    public void onShippingDone(String message) {
        try {
            ShippingDoneEvent shippingDoneEvent = this.queueService.parse(message, ShippingDoneEvent.class);

            if (shippingDoneEvent.success) {
                this.updateStatus(shippingDoneEvent.orderId, OrderStatus.SHIPPED);
            } else {
                this.updateStatus(shippingDoneEvent.orderId, OrderStatus.CANCELLED);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
