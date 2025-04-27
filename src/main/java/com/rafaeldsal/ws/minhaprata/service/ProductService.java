package com.rafaeldsal.ws.minhaprata.service;

import com.rafaeldsal.ws.minhaprata.dto.product.ProductRequestDto;
import com.rafaeldsal.ws.minhaprata.dto.product.ProductResponseDto;
import com.rafaeldsal.ws.minhaprata.model.jpa.Product;
import org.springframework.data.domain.Page;

public interface ProductService {

  Page<ProductResponseDto> findAll(Integer page, Integer size, String sort, String name);

  ProductResponseDto findById(String productId);

  Product create(ProductRequestDto product);

  ProductResponseDto update(String productId, ProductRequestDto product);

  void delete(String productId);
}
