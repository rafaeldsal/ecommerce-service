package com.rafaeldsal.ws.minhaprata.mapper;

import com.rafaeldsal.ws.minhaprata.dto.OrderItemDto;
import com.rafaeldsal.ws.minhaprata.exception.NotFoundException;
import com.rafaeldsal.ws.minhaprata.model.jpa.Order;
import com.rafaeldsal.ws.minhaprata.model.jpa.OrderItem;
import com.rafaeldsal.ws.minhaprata.model.jpa.Product;
import com.rafaeldsal.ws.minhaprata.repository.jpa.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {

  @Autowired
  public ProductRepository productRepository;

  public OrderItem toEntity(Order order, OrderItemDto dto, Product product) {
    return OrderItem.builder()
        .quantity(dto.quantity())
        .priceAtPurchase(product.getPrice())
        .product(product)
        .order(order)
        .build();
  }

  public OrderItem updateEntity(OrderItem existing, OrderItemDto dto) {
    var product = productRepository.findById(dto.productId())
        .orElseThrow(() -> new NotFoundException("Produto não encontrado"));

    existing.setQuantity(dto.quantity());
    existing.setPriceAtPurchase(product.getPrice());
    existing.setProduct(product);

    return existing;
  }
}
