package com.rafaeldsal.ws.minhaprata.service.impl;

import com.rafaeldsal.ws.minhaprata.dto.product.ProductRequestDto;
import com.rafaeldsal.ws.minhaprata.dto.product.ProductResponseDto;
import com.rafaeldsal.ws.minhaprata.exception.BadRequestException;
import com.rafaeldsal.ws.minhaprata.exception.NotFoundException;
import com.rafaeldsal.ws.minhaprata.mapper.product.ProductMapper;
import com.rafaeldsal.ws.minhaprata.model.jpa.Category;
import com.rafaeldsal.ws.minhaprata.model.jpa.Product;
import com.rafaeldsal.ws.minhaprata.repository.jpa.CategoryRepository;
import com.rafaeldsal.ws.minhaprata.repository.jpa.ProductRepository;
import com.rafaeldsal.ws.minhaprata.service.ProductService;
import com.rafaeldsal.ws.minhaprata.utils.SortUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;

  @Override
  public Page<ProductResponseDto> findAll(Integer page, Integer size, String sort, String name) {

    Pageable pageable = PageRequest.of(page, size, Sort.by(SortUtils.getSortDirection(sort), "name"));

    Page<Product> products;

    if (name != null && !name.trim().isEmpty()) {
      products = productRepository.findByNameContainingIgnoreCase(name, pageable);
    } else {
      products = productRepository.findAll(pageable);
    }

    return products.map(ProductMapper::fromEntityToResponseDto);
  }

  @Override
  public ProductResponseDto findById(String productId) {
    return ProductMapper.fromEntityToResponseDto(getProduct(productId));
  }

  @Override
  public ProductResponseDto create(ProductRequestDto dto) {

    if (Objects.nonNull(dto.id())) {
      throw new BadRequestException("productId não pode ser informado");
    }

    var categoryTypeOpt = getCategory(dto.categoryId());

    var product = productRepository.save(ProductMapper.fromDtoToEntity(dto, categoryTypeOpt));

    return ProductMapper.fromEntityToResponseDto(product);
  }

  @Override
  public ProductResponseDto update(String productId, ProductRequestDto product) {

    var productExisting = getProduct(productId);
    Category category = null;

    if (product.categoryId() != null) {
      category = categoryRepository.findById(product.categoryId())
          .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));
    }

    ProductMapper.updateEntityFromDto(product, productExisting, category);
    productRepository.save(productExisting);

    return ProductMapper.fromEntityToResponseDto(productExisting);
  }

  @Override
  public void delete(String productId) {
    getProduct(productId);
    productRepository.deleteById(productId);
  }

  private Product getProduct(String productId) {
    return productRepository.findById(productId).orElseThrow(
        () -> new NotFoundException("Produto não encontrado")
    );
  }

  private Category getCategory(String categoryId) {
    return categoryRepository.findById(categoryId).orElseThrow(
        () -> new NotFoundException("categoryId não encontrado")
    );
  }
}
