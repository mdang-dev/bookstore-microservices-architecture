package com.mddev.bookstore.catalog.web.controllers;

import com.mddev.bookstore.catalog.domain.PageResult;
import com.mddev.bookstore.catalog.domain.Product;
import com.mddev.bookstore.catalog.domain.ProductNotFoundException;
import com.mddev.bookstore.catalog.domain.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
class ProductController {

  private final ProductService productService;

  @GetMapping
  PageResult<Product> getProducts(@RequestParam(name = "page", defaultValue = "1") int pageNo) {
    return productService.getProducts(pageNo);
  }

  @GetMapping("/{code}")
  ResponseEntity<Product> getProductByCode(@PathVariable String code) {
    return productService
        .getProductByCode(code)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> ProductNotFoundException.forCode(code));
  }
}
