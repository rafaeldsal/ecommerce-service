package com.rafaeldsal.ws.minhaprata.service;

import com.rafaeldsal.ws.minhaprata.dto.ProductDto;
import com.rafaeldsal.ws.minhaprata.model.Product;

import java.util.List;

public interface ProductService {

  List<Product> findAll();

  Product findById(Long id);

  Product create(ProductDto product);

  Product update(Long id, ProductDto product);

  void delete(Long id);
}
