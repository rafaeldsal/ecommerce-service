package com.rafaeldsal.ws.minhaprata.controller;

import com.rafaeldsal.ws.minhaprata.dto.ProductDto;
import com.rafaeldsal.ws.minhaprata.dto.ProductResponseDto;
import com.rafaeldsal.ws.minhaprata.model.Product;
import com.rafaeldsal.ws.minhaprata.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

  @Autowired
  private ProductService productService;

  @GetMapping
  public ResponseEntity<Page<ProductResponseDto>> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                          @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                          @RequestParam(value = "sort", defaultValue = "ASC") String sort,
                                                          @RequestParam(value = "name", required = false) String name) {
    Page<ProductResponseDto> productResponseDtos = productService.findAll(page, size, sort, name);

    return ResponseEntity.ok(productResponseDtos);
  }

  @GetMapping("{id}")
  public ResponseEntity<ProductResponseDto> findById(@PathVariable("id") Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(productService.findById(id));
  }

  @PostMapping
  public ResponseEntity<Product> create(@Valid @RequestBody ProductDto product) {
    return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(product));
  }

  @PutMapping("{id}")
  public ResponseEntity<ProductResponseDto> update(@RequestBody ProductDto product, @PathVariable("id") Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(productService.update(id, product));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
    productService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

}
