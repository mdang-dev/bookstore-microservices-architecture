package com.mddev.bookstore.orders.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "order_items")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "order")
@EqualsAndHashCode(exclude = "order")
class OrderItemEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_item_id_generator")
  @SequenceGenerator(name = "order_item_id_generator", sequenceName = "order_item_id_seq")
  Long id;

  @Column(nullable = false)
  String code;

  String name;

  @Column(nullable = false)
  BigDecimal price;

  @Column(nullable = false)
  Integer quantity;

  @ManyToOne(optional = false)
  @JoinColumn(name = "order_id")
  @JsonBackReference
  OrderEntity order;
}
