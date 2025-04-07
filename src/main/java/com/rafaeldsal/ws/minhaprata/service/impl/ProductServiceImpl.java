package com.rafaeldsal.ws.minhaprata.service.impl;

import com.rafaeldsal.ws.minhaprata.dto.ProductDto;
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
  public List<Product> findAll() {
    return productRepository.findAll();
  }

  @Override
  public Product findById(Long id) {
    return getProduct(id);
  }

  @Override
  public Product create(ProductDto dto) {

    if (Objects.nonNull(dto.getId())) {
      throw new BadRequestException("productId não pode ser informado");
    }

    var categoryTypeOpt = getCategory(dto.getCategoryId());

    var product = ProductMapper.fromDtoToEntity(dto, categoryTypeOpt);

    return productRepository.save(product);
  }

  @Override
  public Product update(Long id, ProductDto product) {

    var productExisting = getProduct(id);
    product.setId(productExisting.getId());

    return productRepository.save(ProductMapper.fromDtoToEntity(product, productExisting.getCategory()));
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
