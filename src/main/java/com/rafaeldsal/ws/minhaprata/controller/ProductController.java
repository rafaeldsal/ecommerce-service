package com.rafaeldsal.ws.minhaprata.controller;

import com.rafaeldsal.ws.minhaprata.dto.product.ProductRequestDto;
import com.rafaeldsal.ws.minhaprata.dto.product.ProductResponseDto;
import com.rafaeldsal.ws.minhaprata.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @GetMapping
  public ResponseEntity<Page<ProductResponseDto>> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                          @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                          @RequestParam(value = "sort", defaultValue = "ASC") String sort,
                                                          @RequestParam(value = "name", required = false) String name) {
    Page<ProductResponseDto> productResponseDtos = productService.findAll(page, size, sort, name);

    return ResponseEntity.ok(productResponseDtos);
  }

  @GetMapping("{productId}")
  public ResponseEntity<ProductResponseDto> findById(@PathVariable("productId") String productId) {
    return ResponseEntity.status(HttpStatus.OK).body(productService.findById(productId));
  }

//  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<ProductResponseDto> create(@Valid @RequestBody ProductRequestDto product) {
    return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(product));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("{productId}")
  public ResponseEntity<ProductResponseDto> update(@RequestBody ProductRequestDto product, @PathVariable("productId") String productId) {
    return ResponseEntity.status(HttpStatus.OK).body(productService.update(productId, product));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("{productId}")
  public ResponseEntity<Void> delete(@PathVariable("productId") String productId) {
    productService.delete(productId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
