package com.mddev.bookstore.orders.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mddev.bookstore.orders.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {
  private final ApplicationProperties properties;

  @Bean
  DirectExchange exchange() {
    return new DirectExchange(properties.orderEventsExchange());
  }

  @Bean
  Queue newOrdersQueue() {
    return QueueBuilder.durable(properties.newOrdersQueue()).build();
  }

  @Bean
  Binding newOrdersQueueBinding() {
    return BindingBuilder.bind(newOrdersQueue()).to(exchange()).with(properties.newOrdersQueue());
  }

  @Bean
  Queue cancelledOrdersQueue() {
    return QueueBuilder.durable(properties.cancelledOrdersQueue()).build();
  }

  @Bean
  Binding cancelledOrdersQueuBinding() {
    return BindingBuilder.bind(cancelledOrdersQueue())
        .to(exchange())
        .with(properties.cancelledOrdersQueue());
  }

  @Bean
  Queue errorOrdersQueue() {
    return QueueBuilder.durable(properties.errorOrdersQueue()).build();
  }

  @Bean
  Binding errorOrdersQueuBinding() {
    return BindingBuilder.bind(cancelledOrdersQueue())
        .to(exchange())
        .with(properties.errorOrdersQueue());
  }

  @Bean
  Queue deliveredOrdersQueue() {
    return QueueBuilder.durable(properties.deliveredOrdersQueue()).build();
  }

  @Bean
  Binding deliveredOrdersQueuBinding() {
    return BindingBuilder.bind(deliveredOrdersQueue())
        .to(exchange())
        .with(properties.deliveredOrdersQueue());
  }

  @Bean
  public RabbitTemplate rabbitTemplate(
      ConnectionFactory connectionFactory, ObjectMapper objectMapper) {
    final var rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(jacksonConverter(objectMapper));
    return rabbitTemplate;
  }

  @Bean
  public Jackson2JsonMessageConverter jacksonConverter(ObjectMapper mapper) {
    return new Jackson2JsonMessageConverter(mapper);
  }
}
