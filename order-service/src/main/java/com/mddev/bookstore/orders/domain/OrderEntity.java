package com.mddev.bookstore.orders.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mddev.bookstore.orders.domain.models.Address;
import com.mddev.bookstore.orders.domain.models.Customer;
import com.mddev.bookstore.orders.domain.models.OrderStatus;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "orders")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "items")
@EqualsAndHashCode(exclude = "items")
class OrderEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_id_generator")
  @SequenceGenerator(name = "order_id_generator", sequenceName = "order_id_seq")
  Long id;

  @Column(nullable = false)
  String orderNumber;

  @Column(nullable = false)
  String username;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
  @JsonManagedReference
  Set<OrderItemEntity> items;

  @Embedded
  @AttributeOverrides(
      value = {
        @AttributeOverride(
            name = "name",
            column = @Column(name = "customer_name", nullable = false)),
        @AttributeOverride(
            name = "email",
            column = @Column(name = "customer_email", nullable = false)),
        @AttributeOverride(
            name = "phone",
            column = @Column(name = "customer_phone", nullable = false))
      })
  Customer customer;

  @Embedded
  @AttributeOverrides(
      value = {
        @AttributeOverride(
            name = "addressLine1",
            column = @Column(name = "delivery_address_line1")),
        @AttributeOverride(
            name = "addressLine2",
            column = @Column(name = "delivery_address_line2")),
        @AttributeOverride(name = "city", column = @Column(name = "delivery_city")),
        @AttributeOverride(name = "state", column = @Column(name = "delivery_state")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "delivery_zip_code")),
        @AttributeOverride(name = "country", column = @Column(name = "delivery_country"))
      })
  Address deliveryAddress;

  @Enumerated(EnumType.STRING)
  OrderStatus status;

  String comments;

  @Column(name = "create_at", nullable = false, updatable = false)
  @Builder.Default
  LocalDateTime createAt = LocalDateTime.now();

  @Column(name = "update_at")
  LocalDateTime updateAt;
}
