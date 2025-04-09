package com.rafaeldsal.ws.minhaprata.service.impl;

import com.rafaeldsal.ws.minhaprata.dto.ProductDto;
import com.rafaeldsal.ws.minhaprata.dto.ProductResponseDto;
import com.rafaeldsal.ws.minhaprata.exception.BadRequestException;
import com.rafaeldsal.ws.minhaprata.exception.NotFoundException;
import com.rafaeldsal.ws.minhaprata.mapper.ProductMapper;
import com.rafaeldsal.ws.minhaprata.model.Category;
import com.rafaeldsal.ws.minhaprata.model.Product;
import com.rafaeldsal.ws.minhaprata.repository.CategoryRepository;
import com.rafaeldsal.ws.minhaprata.repository.ProductRepository;
import com.rafaeldsal.ws.minhaprata.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ProductServiceImpl implements ProductService {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  @Override
  public List<ProductResponseDto> findAll() {
    return productRepository.findAll().stream()
        .map(ProductMapper::fromEntityToResponseDto)
        .toList();
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

    System.out.println("DTO Img url: " + product.imgUrl());
    System.out.println("Entity Img url: " + productExisting.getImgUrl());

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
