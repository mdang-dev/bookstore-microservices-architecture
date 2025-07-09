package com.mddev.bookstore.notifications.events;

import com.mddev.bookstore.notifications.domain.NotificationService;
import com.mddev.bookstore.notifications.domain.OrderEventEntity;
import com.mddev.bookstore.notifications.domain.OrderEventRepository;
import com.mddev.bookstore.notifications.domain.models.OrderCancelledEvent;
import com.mddev.bookstore.notifications.domain.models.OrderCreatedEvent;
import com.mddev.bookstore.notifications.domain.models.OrderDeliveredEvent;
import com.mddev.bookstore.notifications.domain.models.OrderErrorEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventHandler {

  private final NotificationService notificationService;
  private final OrderEventRepository orderEventRepository;

  @RabbitListener(queues = "${notifications.new-orders-queue}")
  void handleOrderCreatedEvent(OrderCreatedEvent event) {
    if (orderEventRepository.existsByEventId(event.eventId())) {
      log.warn("Received duplicate OrderCreatedEvent with eventId: {}", event.eventId());
      return;
    }
    log.info("Received a OrderCreatedEvent with orderNumber: {}", event.orderNumber());
    notificationService.sendOrderCreatedNotification(event);
    orderEventRepository.save(OrderEventEntity.builder().eventId(event.eventId()).build());
  }

  @RabbitListener(queues = "${notifications.delivered-orders-queue}")
  void handleOrderDeliveredEvent(OrderDeliveredEvent event) {
    if (orderEventRepository.existsByEventId(event.eventId())) {
      log.warn("Received duplicate OrderDeliveredEvent with eventId: {}", event.eventId());
      return;
    }
    log.info("Received a OrderDeliveredEvent with orderNumber: {}", event.orderNumber());
    notificationService.sendOrderDeliveredNotification(event);
    orderEventRepository.save(OrderEventEntity.builder().eventId(event.eventId()).build());
  }

  @RabbitListener(queues = "${notifications.cancelled-orders-queue}")
  void handleOrderCancelledEvent(OrderCancelledEvent event) {
    if (orderEventRepository.existsByEventId(event.eventId())) {
      log.warn("Received duplicate OrderCancelledEvent with eventId: {}", event.eventId());
      return;
    }
    log.info("Received a OrderCancelledEvent with orderNumber: {}", event.orderNumber());
    notificationService.sendOrderCancelledNotification(event);
    orderEventRepository.save(OrderEventEntity.builder().eventId(event.eventId()).build());
  }

  @RabbitListener(queues = "${notifications.error-orders-queue}")
  void handleOrderErrorEvent(OrderErrorEvent event) {
    if (orderEventRepository.existsByEventId(event.eventId())) {
      log.warn("Received duplicate OrderErrorEvent with eventId: {}", event.eventId());
      return;
    }
    log.info("Received a OrderErrorEvent with orderNumber: {}", event.orderNumber());
    notificationService.sendOrderErrorNotification(event);
    orderEventRepository.save(OrderEventEntity.builder().eventId(event.eventId()).build());
  }
}
