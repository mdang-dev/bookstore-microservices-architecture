package com.mddev.bookstore.catalog.domain;

import com.mddev.bookstore.catalog.ApplicatioinProperties;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
  private final ProductRepository productRepository;
  private final ApplicatioinProperties properties;

  public PageResult<Product> getProducts(int pageNo) {
    Sort sort = Sort.by("name").ascending();
    pageNo = pageNo <= 1 ? 0 : pageNo - 1;
    Pageable pageable = PageRequest.of(pageNo, properties.pageSize(), sort);
    Page<Product> productsPage = productRepository.findAll(pageable).map(ProductMapper::toProduct);
    return new PageResult<>(
        productsPage.getContent(),
        productsPage.getTotalElements(),
        productsPage.getNumber() + 1,
        productsPage.getTotalPages(),
        productsPage.isFirst(),
        productsPage.isLast(),
        productsPage.hasNext(),
        productsPage.hasPrevious());
  }

  public Optional<Product> getProductByCode(String code) {
    return productRepository.findByCode(code).map(ProductMapper::toProduct);
  }
}
