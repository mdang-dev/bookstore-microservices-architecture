package com.mddev.bookstore.orders.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mddev.bookstore.orders.domain.models.OrderCancelledEvent;
import com.mddev.bookstore.orders.domain.models.OrderCreatedEvent;
import com.mddev.bookstore.orders.domain.models.OrderDeliveredEvent;
import com.mddev.bookstore.orders.domain.models.OrderErrorEvent;
import com.mddev.bookstore.orders.domain.models.OrderEventType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OrderEventService {

  private final OrderEventRepository orderEventRepository;
  private final ObjectMapper objectMapper;
  private final OrderEventPublisher orderEventPublisher;

  void save(OrderCreatedEvent event) {
    OrderEventEntity orderEvent =
        OrderEventEntity.builder()
            .eventId(event.eventId())
            .eventType(OrderEventType.ORDER_CREATED)
            .orderNumber(event.orderNumber())
            .createdAt(event.createdAt())
            .payload(toJsonPayload(event))
            .build();
    orderEventRepository.save(orderEvent);
  }

  void save(OrderDeliveredEvent event) {
    OrderEventEntity orderEvent =
        OrderEventEntity.builder()
            .eventId(event.eventId())
            .eventType(OrderEventType.ORDER_DELIVERED)
            .orderNumber(event.orderNumber())
            .createdAt(event.createdAt())
            .payload(toJsonPayload(event))
            .build();
    orderEventRepository.save(orderEvent);
  }

  void save(OrderCancelledEvent event) {
    OrderEventEntity orderEvent =
        OrderEventEntity.builder()
            .eventId(event.eventId())
            .eventType(OrderEventType.ORDER_CANCELLED)
            .orderNumber(event.orderNumber())
            .createdAt(event.createdAt())
            .payload(toJsonPayload(event))
            .build();
    orderEventRepository.save(orderEvent);
  }

  void save(OrderErrorEvent event) {
    OrderEventEntity orderEvent =
        OrderEventEntity.builder()
            .eventId(event.eventId())
            .eventType(OrderEventType.ORDER_PROCESSING_FAILED)
            .orderNumber(event.orderNumber())
            .createdAt(event.createdAt())
            .payload(toJsonPayload(event))
            .build();
    orderEventRepository.save(orderEvent);
  }

  public void publishOrderEvents() {
    Sort sort = Sort.by("createdAt").ascending();
    List<OrderEventEntity> events = orderEventRepository.findAll(sort);
    log.info("Found {} Order Events to be published", events.size());
    for (OrderEventEntity event : events) {
      log.info("Info", events);
      publishEvent(event);
      orderEventRepository.delete(event);
    }
  }

  private void publishEvent(OrderEventEntity event) {

    OrderEventType eventType = event.getEventType();

    switch (eventType) {
      case ORDER_CREATED:
        OrderCreatedEvent orderCreatedEvent =
            fromJsonPayload(event.getPayload(), OrderCreatedEvent.class);
        orderEventPublisher.publish(orderCreatedEvent);
        break;
      case ORDER_DELIVERED:
        OrderDeliveredEvent orderDeliveredEvent =
            fromJsonPayload(event.getPayload(), OrderDeliveredEvent.class);
        orderEventPublisher.publish(orderDeliveredEvent);
        break;
      case ORDER_CANCELLED:
        OrderCancelledEvent orderCancelledEvent =
            fromJsonPayload(event.getPayload(), OrderCancelledEvent.class);
        orderEventPublisher.publish(orderCancelledEvent);
        break;
      case ORDER_PROCESSING_FAILED:
        OrderErrorEvent orderErrorEvent =
            fromJsonPayload(event.getPayload(), OrderErrorEvent.class);
        orderEventPublisher.publish(orderErrorEvent);
        break;
      default:
        log.warn("Unsupport OrderEventType: {}", eventType);
        break;
    }
  }

  private String toJsonPayload(Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  private <T> T fromJsonPayload(String json, Class<T> type) {
    try {
      return objectMapper.readValue(json, type);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
