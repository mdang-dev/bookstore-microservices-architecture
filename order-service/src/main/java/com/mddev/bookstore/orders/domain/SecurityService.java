package com.mddev.bookstore.orders.domain;

import org.springframework.stereotype.Service;

@Service
public class SecurityService {

  public String getLoginUsername() {
    return "user";
  }
}
