package com.mddev.bookstore.orders.domain;

import com.mddev.bookstore.orders.ApplicationProperties;
import com.mddev.bookstore.orders.domain.models.OrderCancelledEvent;
import com.mddev.bookstore.orders.domain.models.OrderCreatedEvent;
import com.mddev.bookstore.orders.domain.models.OrderDeliveredEvent;
import com.mddev.bookstore.orders.domain.models.OrderErrorEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class OrderEventPublisher {

  private final RabbitTemplate rabbitTemplate;
  private final ApplicationProperties properties;

  public void publish(OrderCreatedEvent event) {
    send(properties.newOrdersQueue(), event);
  }

  public void publish(OrderDeliveredEvent event) {
    send(properties.deliveredOrdersQueue(), event);
  }

  public void publish(OrderCancelledEvent event) {
    send(properties.cancelledOrdersQueue(), event);
  }

  public void publish(OrderErrorEvent event) {
    send(properties.errorOrdersQueue(), event);
  }

  private void send(String routingKey, Object payload) {
    rabbitTemplate.convertAndSend(properties.orderEventsExchange(), routingKey, payload);
  }
}
