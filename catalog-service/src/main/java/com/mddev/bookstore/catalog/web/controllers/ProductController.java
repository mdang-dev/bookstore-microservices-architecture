package com.mddev.bookstore.catalog.web.controllers;

import com.mddev.bookstore.catalog.domain.PageResult;
import com.mddev.bookstore.catalog.domain.Product;
import com.mddev.bookstore.catalog.domain.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
class ProductController {

  private final ProductService productService;

  ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  PageResult<Product> getProducts(@RequestParam(name = "page", defaultValue = "1") int pageNo) {
    return productService.getProducts(pageNo);
  }
}
