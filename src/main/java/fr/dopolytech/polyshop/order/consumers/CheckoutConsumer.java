package fr.dopolytech.polyshop.order.consumers;

import java.util.Arrays;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import fr.dopolytech.polyshop.order.domain.commands.CheckoutCommand;
import fr.dopolytech.polyshop.order.domain.commands.CreateProductCommand;
import fr.dopolytech.polyshop.order.domain.entities.OrderStatus;
import fr.dopolytech.polyshop.order.services.OrderService;
import fr.dopolytech.polyshop.order.services.ProductService;
import fr.dopolytech.polyshop.order.services.QueueService;
import jakarta.transaction.Transactional;

@Component
public class CheckoutConsumer {
    private final QueueService queueService;
    private final OrderService orderService;
    private final ProductService productService;

    public CheckoutConsumer(QueueService queueService, OrderService orderService, ProductService productService) {
        this.queueService = queueService;
        this.orderService = orderService;
        this.productService = productService;
    }

    @Transactional
    private CheckoutCommand handleCreateOrderCommand(CheckoutCommand command) throws Exception {
        String orderId = this.orderService.createOrder();
        Arrays.asList(command.products)
                .stream()
                .forEach(commandProduct -> {
                    CreateProductCommand createProductCommand = new CreateProductCommand(commandProduct.id,
                            commandProduct.name, commandProduct.price, commandProduct.quantity);
                    try {
                        this.productService.createProduct(orderId, createProductCommand);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                });

        return new CheckoutCommand(orderId, command.products);
    }

    @RabbitListener(queues = "createOrderQueue")
    public void onCartCheckout(String message) {
        try {
            CheckoutCommand inputCommand = this.queueService.parse(message, CheckoutCommand.class);
            CheckoutCommand outputCommand = this.handleCreateOrderCommand(inputCommand);
            this.queueService.send(outputCommand, "order.created");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "checkSuccessOrderQueue")
    public void onInventoryUpdateSuccess(String message) {
        try {
            CheckoutCommand command = this.queueService.parse(message, CheckoutCommand.class);

            if (command.orderId != null) {
                this.orderService.updateStatus(command.orderId, OrderStatus.CHECKED);
                this.queueService.send(command, "order.checked");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "checkFailedOrderQueue")
    public void onInventoryUpdateFailed(String message) {
        try {
            CheckoutCommand command = this.queueService.parse(message, CheckoutCommand.class);

            if (command.orderId != null) {
                this.orderService.updateStatus(command.orderId, OrderStatus.CHECK_FAILED);
                this.queueService.send(command, "order.cancelled.check");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "paymentSuccessOrderQueue")
    public void onPaymentDoneSuccess(String message) {
        try {
            CheckoutCommand command = this.queueService.parse(message, CheckoutCommand.class);

            if (command.orderId != null) {
                this.orderService.updateStatus(command.orderId, OrderStatus.PAID);
                this.queueService.send(command, "order.paid");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "paymentFailedOrderQueue")
    public void onPaymentDoneFailed(String message) {
        try {
            CheckoutCommand command = this.queueService.parse(message, CheckoutCommand.class);

            if (command.orderId != null) {
                this.orderService.updateStatus(command.orderId, OrderStatus.PAYMENT_FAILED);
                this.queueService.send(command, "order.cancelled.payment");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "shippingSuccessOrderQueue")
    public void onShippingDoneSuccess(String message) {
        try {
            CheckoutCommand command = this.queueService.parse(message, CheckoutCommand.class);

            if (command.orderId != null) {
                this.orderService.updateStatus(command.orderId, OrderStatus.SHIPPED);
                this.queueService.send(command, "order.shipped");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "shippingFailedOrderQueue")
    public void onShippingDoneFailed(String message) {
        try {
            CheckoutCommand command = this.queueService.parse(message, CheckoutCommand.class);

            if (command.orderId != null) {
                this.orderService.updateStatus(command.orderId, OrderStatus.SHIPPING_FAILED);
                this.queueService.send(command, "order.cancelled.shipping");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
