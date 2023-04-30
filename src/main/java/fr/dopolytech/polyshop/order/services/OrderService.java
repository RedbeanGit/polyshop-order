package fr.dopolytech.polyshop.order.services;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import fr.dopolytech.polyshop.order.dtos.CreateProductDto;
import fr.dopolytech.polyshop.order.models.Order;
import fr.dopolytech.polyshop.order.models.OrderStatus;
import fr.dopolytech.polyshop.order.models.PolyshopEvent;
import fr.dopolytech.polyshop.order.models.PolyshopEventProduct;
import fr.dopolytech.polyshop.order.models.Product;
import fr.dopolytech.polyshop.order.repositories.OrderRepository;
import jakarta.transaction.Transactional;

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

    @Transactional
    public Order updateStatus(String orderId, OrderStatus status) {
        Order order = this.getOrder(orderId);
        order.status = status;
        return this.orderRepository.save(order);
    }

    @Transactional
    public PolyshopEvent handleCreateEvent(PolyshopEvent event) {
        Order order = this.createOrder();
        List<PolyshopEventProduct> outputEventProducts = Arrays.asList(event.products)
                .stream()
                .map(eventProduct -> {
                    CreateProductDto createProductDto = new CreateProductDto(eventProduct.id, eventProduct.name,
                            eventProduct.price, eventProduct.quantity);
                    Product product = this.productService.createProduct(order.orderId, createProductDto);
                    return new PolyshopEventProduct(product.productId, product.name, product.price,
                            product.quantity);
                })
                .toList();

        return new PolyshopEvent(order.orderId,
                outputEventProducts.toArray(new PolyshopEventProduct[outputEventProducts.size()]));
    }

    @RabbitListener(queues = "createOrderQueue")
    public void onCartCheckout(String message) {
        try {
            PolyshopEvent inputEvent = this.queueService.parse(message);
            PolyshopEvent outputEvent = this.handleCreateEvent(inputEvent);
            this.queueService.sendCreated(outputEvent);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "checkSuccessOrderQueue")
    public void onInventoryUpdateSuccess(String message) {
        try {
            PolyshopEvent event = this.queueService.parse(message);

            if (event.orderId != null) {
                this.updateStatus(event.orderId, OrderStatus.CHECKED);
                this.queueService.sendChecked(event);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "checkFailedOrderQueue")
    public void onInventoryUpdateFailed(String message) {
        try {
            PolyshopEvent event = this.queueService.parse(message);

            if (event.orderId != null) {
                this.updateStatus(event.orderId, OrderStatus.CHECK_FAILED);
                this.queueService.sendCancelledCheck(event);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "paymentSuccessOrderQueue")
    public void onPaymentDoneSuccess(String message) {
        try {
            PolyshopEvent event = this.queueService.parse(message);

            if (event.orderId != null) {
                this.updateStatus(event.orderId, OrderStatus.PAID);
                this.queueService.sendPaid(event);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "paymentFailedOrderQueue")
    public void onPaymentDoneFailed(String message) {
        try {
            PolyshopEvent event = this.queueService.parse(message);

            if (event.orderId != null) {
                this.updateStatus(event.orderId, OrderStatus.PAYMENT_FAILED);
                this.queueService.sendCancelledPayment(event);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "shippingSuccessOrderQueue")
    public void onShippingDoneSuccess(String message) {
        try {
            PolyshopEvent event = this.queueService.parse(message);

            if (event.orderId != null) {
                this.updateStatus(event.orderId, OrderStatus.SHIPPED);
                this.queueService.sendShipped(event);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "shippingSuccessOrderQueue")
    public void onShippingDoneFailed(String message) {
        try {
            PolyshopEvent event = this.queueService.parse(message);

            if (event.orderId != null) {
                this.updateStatus(event.orderId, OrderStatus.SHIPPING_FAILED);
                this.queueService.sendCancelledShipping(event);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
