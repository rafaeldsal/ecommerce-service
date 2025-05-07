package com.rafaeldsal.ws.minhaprata.mapper.payment;

import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentDto;
import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentErrorInfo;
import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentInitResponseDto;
import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentRecord;
import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentUpdateStatus;
import com.rafaeldsal.ws.minhaprata.dto.payment.PaymentWebhookResponseDto;
import com.rafaeldsal.ws.minhaprata.exception.BusinessException;
import com.rafaeldsal.ws.minhaprata.model.enums.PaymentStatus;
import com.rafaeldsal.ws.minhaprata.model.jpa.Order;
import com.rafaeldsal.ws.minhaprata.model.jpa.Payment;
import com.rafaeldsal.ws.minhaprata.utils.DateTimeUtils;

import java.math.BigDecimal;

public class PaymentMapper {

  private PaymentMapper() {}

  public static Payment toEntity(PaymentDto dto, Order order, String paymentId, String transactionId) {
    return Payment.builder()
        .id(paymentId)
        .transactionId(transactionId)
        .amount(order.getTotalPrice())
        .paymentMethod(dto.paymentMethod())
        .order(order)
        .currency(dto.currency())
        .dtCreated(DateTimeUtils.now())
        .dtUpdated(DateTimeUtils.now())
        .status(PaymentStatus.REQUIRES_PAYMENT_METHOD)
        .savePaymentMethod(dto.savePaymentMethod())
        .build();
  }

  public static PaymentInitResponseDto toResponseDto(Payment payment) {
    return PaymentInitResponseDto.builder()
        .id(payment.getId())
        .transactionId(payment.getTransactionId())
        .status(payment.getStatus().toString())
        .build();
  }

  public static PaymentRecord toEventDto(Order order, Payment payment) {
    return PaymentRecord.builder()
        .transactionId(payment.getTransactionId())
        .timestamp(DateTimeUtils.timestamp())
        .paymentMethod(payment.getPaymentMethod())
        .amount(payment.getAmount().multiply(BigDecimal.valueOf(100)).longValue())
        .currency(payment.getCurrency())
        .orderId(order.getId())
        .userId(order.getUser().getId())
        .build();
  }

  public static void updatedToEntityFinally(PaymentWebhookResponseDto dto, Payment payment, PaymentStatus status) {
    if (dto == null || payment == null) {
      throw new BusinessException("DTO e Payment não podem ser nulos");
    }
    payment.setStatus(status);
    payment.setDtUpdated(DateTimeUtils.now());

    if (dto.paymentErrorInfo() != null) {
      payment.setFailedAt(DateTimeUtils.now());
      payment.setFailureCode(dto.paymentErrorInfo().errorCode());
      payment.setDeclineCode(dto.paymentErrorInfo().stripeFailureCode());
      payment.setFailureMessage(dto.paymentErrorInfo().errorMessage());
    }
  }

  public static void updatedToEntity(PaymentUpdateStatus dto, Payment payment) {
    if (dto == null || payment == null) {
      throw new BusinessException("DTO e Payment não podem ser nulos");
    }

    payment.setStatus(PaymentStatus.valueOf(dto.status()));
    payment.setDtUpdated(DateTimeUtils.now());

    if (dto.paymentIntentId() != null) {
      payment.setPaymentIntentId(dto.paymentIntentId());
    }

    if (dto.clientSecret() != null) {
      payment.setClientSecret(dto.clientSecret());
    }
  }

  public static PaymentUpdateStatus paymentProcessingError(Payment payment) {
    Order order = payment.getOrder();
    String orderId = order != null ? order.getId() : null;
    String userId = (order != null && order.getUser() != null) ? order.getUser().getId() : null;

    return PaymentUpdateStatus.builder()
        .paymentId(payment.getId())
        .paymentIntentId(payment.getPaymentIntentId())
        .clientSecret(payment.getClientSecret())
        .transactionId(payment.getTransactionId())
        .orderId(orderId)
        .status(payment.getStatus().toString())
        .userId(userId)
        .paymentErrorInfo(PaymentErrorInfo.builder()
            .errorCode(payment.getFailureCode())
            .errorMessage(payment.getFailureMessage())
            .stripeFailureCode(payment.getDeclineCode())
            .build())
        .build();
  }

  public static PaymentUpdateStatus paymentProcessed(Payment payment) {
    Order order = payment.getOrder();
    String orderId = order != null ? order.getId() : null;
    String userId = (order != null && order.getUser() != null) ? order.getUser().getId() : null;

    return PaymentUpdateStatus.builder()
        .paymentId(payment.getId())
        .paymentIntentId(payment.getPaymentIntentId())
        .clientSecret(payment.getClientSecret())
        .transactionId(payment.getTransactionId())
        .orderId(orderId)
        .status(payment.getStatus() != null ? payment.getStatus().toString() : null)
        .userId(userId)
        .build();
  }
}