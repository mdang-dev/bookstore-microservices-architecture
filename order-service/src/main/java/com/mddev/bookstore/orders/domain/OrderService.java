package com.mddev.bookstore.orders.domain;

import com.mddev.bookstore.orders.domain.models.CreateOrderRequest;
import com.mddev.bookstore.orders.domain.models.CreateOrderResponse;
import com.mddev.bookstore.orders.domain.models.OrderCreatedEvent;
import com.mddev.bookstore.orders.domain.models.OrderDTO;
import com.mddev.bookstore.orders.domain.models.OrderStatus;
import com.mddev.bookstore.orders.domain.models.OrderSummary;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

  private final OrderRepository orderRepository;
  private final OrderValidator orderValidator;
  private final OrderEventService orderEventService;
  private static final List<String> DELIVERY_ALLOWED_COUNTRIES =
      List.of("VIETNAMESE", "US", "UK", "GERMANY");

  public CreateOrderResponse createOrder(String username, CreateOrderRequest request) {
    orderValidator.validate(request);
    OrderEntity newOrder = OrderMapper.convertToEntity(request);
    newOrder.setUsername(username);
    OrderEntity savedOrder = orderRepository.save(newOrder);
    log.info("Created Order with orderNumber={}", savedOrder.getOrderNumber());
    OrderCreatedEvent orderCreatedEvent = OrderEventMapper.buildOrderCreatedEvent(savedOrder);
    orderEventService.save(orderCreatedEvent);
    return new CreateOrderResponse(savedOrder.getOrderNumber());
  }

  public List<OrderSummary> findOrders(String userName) {
    return orderRepository.findByUsername(userName);
  }

  public Optional<OrderDTO> findUserOrder(String username, String orderNumber) {
    return orderRepository
        .findByUsernameAndOrderNumber(username, orderNumber)
        .map(OrderMapper::convertToDTO);
  }

  public void processNewOrders() {
    List<OrderEntity> orders = orderRepository.findByStatus(OrderStatus.NEW);
    log.info("Found {} new orders to process", orders.size());
    for (OrderEntity order : orders) {
      process(order);
    }
  }

  private void process(OrderEntity order) {
    try {
      if (canBeDelivered(order)) {
        log.info("OrderNumber: {} can be delivered", order.getOrderNumber());
        orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.DELIVERED);
        orderEventService.save(OrderEventMapper.buildOrderDeliveredEvent(order));
      } else {
        log.info("OrderNumber: {} can't be delivered", order.getOrderNumber());
        orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.CANCELLED);
        orderEventService.save(
            OrderEventMapper.buildOrderCancelledEvent(order, "Can't deliver to the location"));
      }
    } catch (RuntimeException e) {
      log.info("Failed to process Order with orderNumber: {}", order.getOrderNumber(), e);
      orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.ERROR);
      orderEventService.save(OrderEventMapper.buildOrderErrorEvent(order, e.getMessage()));
    }
  }

  private boolean canBeDelivered(OrderEntity order) {
    return DELIVERY_ALLOWED_COUNTRIES.contains(order.getDeliveryAddress().country().toUpperCase());
  }
}
