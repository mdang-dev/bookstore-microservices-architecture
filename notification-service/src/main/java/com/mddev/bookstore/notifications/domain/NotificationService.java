package com.mddev.bookstore.notifications.domain;

import com.mddev.bookstore.notifications.ApplicationProperties;
import com.mddev.bookstore.notifications.domain.models.OrderCancelledEvent;
import com.mddev.bookstore.notifications.domain.models.OrderCreatedEvent;
import com.mddev.bookstore.notifications.domain.models.OrderDeliveredEvent;
import com.mddev.bookstore.notifications.domain.models.OrderErrorEvent;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

  private final JavaMailSender emailSender;
  private final ApplicationProperties properties;

  public void sendOrderCreatedNotification(OrderCreatedEvent event) {
    String message =
        """
                ===========================================
                Order Created Notification
                -------------------------------------------
                Dear %s,
                Your order with orderNumber: %s has been created successfully.

                Thanks,
                Bookstore Team
                ===========================================
                """
            .formatted(event.customer().name(), event.orderNumber());
    log.info("\n{}", message);
    sendEmail(event.customer().email(), "Order Created Notification", message);
  }

  public void sendOrderDeliveredNotification(OrderDeliveredEvent event) {
    String message =
        """
                ===========================================
                Order Delivered Notification
                -------------------------------------------
                Dear %s,
                Your order with orderNumber: %s has been delivered.

                We hope you enjoy your purchase!

                Thanks,
                Bookstore Team
                ===========================================
                """
            .formatted(event.customer().name(), event.orderNumber());
    log.info("\n{}", message);
    sendEmail(event.customer().email(), "Order Delivered Notification", message);
  }

  public void sendOrderCancelledNotification(OrderCancelledEvent event) {
    String message =
        """
                ===========================================
                Order Cancelled Notification
                -------------------------------------------
                Dear %s,
                Your order with orderNumber: %s has been cancelled.

                Can't deliver to location
                If you have any questions, please contact us.

                Thanks,
                Bookstore Team
                ===========================================
                """
            .formatted(event.customer().name(), event.orderNumber());
    log.info("\n{}", message);
    sendEmail(event.customer().email(), "Order Cancelled Notification", message);
  }

  public void sendOrderErrorNotification(OrderErrorEvent event) {
    String message =
        """
                ===========================================
                Order Error Notification
                -------------------------------------------
                Dear %s,
                There was an error processing your order with orderNumber: %s.

                Error Details: %s

                Please contact support for assistance.

                Thanks,
                Bookstore Team
                ===========================================
                """
            .formatted(event.customer().name(), event.orderNumber(), event.reason());
    log.info("\n{}", message);
    sendEmail(properties.supportEmail(), "Order Processing Failure Notification", message);
  }

  private void sendEmail(String recipient, String subject, String content) {

    try {
      MimeMessage mimeMessage = emailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
      helper.setFrom(properties.supportEmail());
      helper.setTo(recipient);
      helper.setSubject(subject);
      helper.setText(content);
      emailSender.send(mimeMessage);
      log.info("Email sent to: {}", recipient);
    } catch (Exception e) {
      throw new RuntimeException("Error while sending email ", e);
    }
  }
}
