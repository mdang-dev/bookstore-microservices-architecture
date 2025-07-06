package com.mddev.bookstore.catalog.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "products")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
class ProductEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_generator")
  @SequenceGenerator(name = "product_id_generator", sequenceName = "product_id_seq")
  Long id;

  @Column(nullable = false, unique = true)
  @NotEmpty(message = "Product code is required")
  String code;

  @Column(nullable = false)
  @NotEmpty(message = "Product name is required")
  String name;

  String description;

  String imageUrl;

  @Column(nullable = false)
  @NotNull(message = "Product price is required")
  @DecimalMin("0.1")
  BigDecimal price;
}
