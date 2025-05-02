package com.rafaeldsal.ws.minhaprata.model.mongo;

import com.rafaeldsal.ws.minhaprata.dto.orderItem.OrderItemResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Document("payment_intent")
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentIntent {

  private String id;
  private String customerId;
  private String orderId;
  private String paymentIntentId;
  private String clientSecret;
  private String currency;
  private String paymentMethodType;
  private String status;
  private BigDecimal amount;
  private LocalDateTime dtRegisterOrder;
  private List<OrderItemResponseDto> orderItem;
}
