package com.rafaeldsal.ws.minhaprata.service.impl;

import com.rafaeldsal.ws.minhaprata.dto.ProductDto;
import com.rafaeldsal.ws.minhaprata.dto.ProductResponseDto;
import com.rafaeldsal.ws.minhaprata.exception.BadRequestException;
import com.rafaeldsal.ws.minhaprata.exception.NotFoundException;
import com.rafaeldsal.ws.minhaprata.mapper.ProductMapper;
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
  public ProductResponseDto findById(Long id) {
    return ProductMapper.fromEntityToResponseDto(getProduct(id));
  }

  @Override
  public Product create(ProductDto dto) {

    if (Objects.nonNull(dto.id())) {
      throw new BadRequestException("productId não pode ser informado");
    }

    var categoryTypeOpt = getCategory(dto.categoryId());

    var product = ProductMapper.fromDtoToEntity(dto, categoryTypeOpt);

    return productRepository.save(product);
  }

  @Override
  public ProductResponseDto update(Long id, ProductDto product) {

    var productExisting = getProduct(id);
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
  public void delete(Long id) {
    getProduct(id);
    productRepository.deleteById(id);
  }

  private Product getProduct(Long id) {
    return productRepository.findById(id).orElseThrow(
        () -> new NotFoundException("Produto não encontrado")
    );
  }

  private Category getCategory(Long id) {
    return categoryRepository.findById(id).orElseThrow(
        () -> new NotFoundException("categoryId não encontrado")
    );
  }
}
