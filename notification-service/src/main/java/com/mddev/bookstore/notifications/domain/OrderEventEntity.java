package com.mddev.bookstore.notifications.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "order_events")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEventEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_event_id_generator")
  @SequenceGenerator(name = "order_event_id_generator", sequenceName = "order_event_id_seq")
  Long id;

  @Column(nullable = false, unique = true)
  String eventId;

  @Builder.Default
  @Column(name = "created_at", nullable = false, updatable = false)
  LocalDateTime createdAt = LocalDateTime.now();

  @Column(name = "updated_at")
  LocalDateTime updatedAt;
}
