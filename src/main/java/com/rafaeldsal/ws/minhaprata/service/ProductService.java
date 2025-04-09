package com.rafaeldsal.ws.minhaprata.service;

import com.rafaeldsal.ws.minhaprata.dto.ProductDto;
import com.rafaeldsal.ws.minhaprata.dto.ProductResponseDto;
import com.rafaeldsal.ws.minhaprata.model.Product;

import java.util.List;

public interface ProductService {

  List<ProductResponseDto> findAll();

  ProductResponseDto findById(Long id);

  Product create(ProductDto product);

  ProductResponseDto update(Long id, ProductDto product);

  void delete(Long id);
}
