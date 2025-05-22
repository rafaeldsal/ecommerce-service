package com.rafaeldsal.ws.minhaprata.service;

import com.rafaeldsal.ws.minhaprata.dto.category.CategoryDto;
import com.rafaeldsal.ws.minhaprata.dto.product.ProductRequestDto;
import com.rafaeldsal.ws.minhaprata.dto.product.ProductResponseDto;
import com.rafaeldsal.ws.minhaprata.exception.BadRequestException;
import com.rafaeldsal.ws.minhaprata.exception.NotFoundException;
import com.rafaeldsal.ws.minhaprata.mapper.product.ProductMapper;
import com.rafaeldsal.ws.minhaprata.model.jpa.Category;
import com.rafaeldsal.ws.minhaprata.model.jpa.Product;
import com.rafaeldsal.ws.minhaprata.repository.jpa.CategoryRepository;
import com.rafaeldsal.ws.minhaprata.repository.jpa.ProductRepository;
import com.rafaeldsal.ws.minhaprata.service.impl.ProductServiceImpl;
import com.rafaeldsal.ws.minhaprata.utils.IdGenerator;
import com.rafaeldsal.ws.minhaprata.utils.SortUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

  private static final String PRODUCT_ID = IdGenerator.UUIDGenerator("prod");
  private static final String CATEGORY_ID = IdGenerator.UUIDGenerator("cat");
  private static final PageRequest PAGE_REQUEST = PageRequest.of(0, 10, Sort.by(SortUtils.getSortDirection("ASC"), "name"));

  @Mock
  private ProductRepository productRepository;

  @Mock
  private CategoryRepository categoryRepository;

  @InjectMocks
  private ProductServiceImpl productService;

  private ProductRequestDto productDto() {
    return ProductRequestDto.builder()
        .name("Produto de teste")
        .price(BigDecimal.valueOf(250.00))
        .categoryId(CATEGORY_ID)
        .imgUrl("https://qualquer-image.com")
        .description("Produto de teste unitário")
        .build();
  }

  private Category category() {
    return Category.builder()
        .description("Categoria de teste")
        .id(CATEGORY_ID)
        .name("Categoria Teste")
        .build();
  }

  private Product loadProduct() {
    return Product.builder()
        .id(PRODUCT_ID)
        .stockQuantity(100L)
        .category(category())
        .price(BigDecimal.valueOf(250.00))
        .dtUpdated(LocalDateTime.of(2025, 4, 23, 19, 50).plusDays(30))
        .dtCreated(LocalDateTime.of(2025, 4, 23, 19, 50).plusDays(30))
        .imgUrl("https://qualquer-image.com")
        .description("Produto de teste unitário")
        .name("Produto de teste")
        .build();
  }

  private ProductResponseDto loadProductResponseDto(Product product) {
    return ProductResponseDto.builder()
        .id(product.getId())
        .category(CategoryDto.builder()
            .description(product.getCategory().getDescription())
            .name(product.getCategory().getName())
            .id(product.getCategory().getId())
            .build())
        .description(product.getDescription())
        .name(product.getName())
        .price(product.getPrice())
        .dtCreated(product.getDtCreated())
        .dtUpdated(product.getDtUpdated())
        .imgUrl(product.getImgUrl())
        .stockQuantity(product.getStockQuantity())
        .build();
  }

  @Test
  void given_findAll_when_nameIsNull_then_returnAllPagedProducts() {
    Pageable pageable = PAGE_REQUEST;
    List<Product> products = List.of(loadProduct());
    Page<Product> productPage = new PageImpl<>(products);

    when(productRepository.findAll(pageable)).thenReturn(productPage);

    Page<ProductResponseDto> result = productService.findAll(0, 10, "UNSORTED", null);

    assertEquals(1, result.getContent().size());
    verify(productRepository, times(1)).findAll(pageable);
  }

  @Test
  void given_findAll_when_nameIsProvided_then_returnFilteredPagedProducts() {
    Pageable pageable = PAGE_REQUEST;
    List<Product> products = List.of(loadProduct());
    Page<Product> productPage = new PageImpl<>(products);

    when(productRepository.findByNameContainingIgnoreCase("Produto", pageable)).thenReturn(productPage);

    var result = productService.findAll(0, 10, "ASC", "Produto");

    assertEquals(1, result.getContent().size());
    verify(productRepository, times(1)).findByNameContainingIgnoreCase("Produto", pageable);
  }

  @Test
  void given_findAll_when_blankName_then_returnAllPagedProducts() {
    Pageable pageable = PAGE_REQUEST;
    List<Product> products = List.of(loadProduct());
    Page<Product> productPage = new PageImpl<>(products);

    when(productRepository.findAll(pageable)).thenReturn(productPage);
    Page<ProductResponseDto> result = productService.findAll(0, 10, "UNSORTED", "");

    assertEquals(1, result.getContent().size());
    verify(productRepository, times(1)).findAll(pageable);
  }

  @Test
  void given_findAll_when_noProductsInDatabase_then_returnEmptyPage() {
    Pageable pageable = PAGE_REQUEST;

    when(productRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of()));

    var result = productService.findAll(0, 10, "UNSORTED", "");

    assertEquals(0, result.getContent().size());
    verify(productRepository, times(1)).findAll(pageable);
  }

  @Test
  void given_findById_when_productExists_then_returnMappedProductResponse() {
    var product = loadProduct();
    var productResponse = loadProductResponseDto(product);

    when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

    assertEquals(productResponse, productService.findById(product.getId()));

    verify(productRepository, times(1)).findById(any());
  }

  @Test
  void given_findById_when_productDoesNotExist_then_throwNotFoundException() {
    when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> productService.findById(PRODUCT_ID));
    verify(productRepository, times(1)).findById(any());
  }

  @Test
  void given_create_when_dataIsValid_then_saveAndReturnMappedProduct() {
    var productDto = productDto();
    var category = category();
    var product = ProductMapper.fromDtoToEntity(productDto, category);
    var productResponse = ProductMapper.fromEntityToResponseDto(product);

    when(categoryRepository.findById(productDto.categoryId())).thenReturn(Optional.of(category()));
    when(productRepository.save(any())).thenReturn(product);

    var response = productService.create(productDto);

    assertNotNull(response);
    assertEquals(product.getId(), response.id());
    verify(productRepository, times(1)).save(any());
    verify(categoryRepository, times(1)).findById(any());
  }

  @Test
  void given_create_when_productIdIsProvided_then_throwBadRequestException() {
    ProductRequestDto dto = ProductRequestDto.builder().id(PRODUCT_ID).build();
    assertThrows(BadRequestException.class, () -> productService.create(dto));

    verify(productRepository, times(0)).save(any());
    verify(categoryRepository, times(0)).findById(any());
  }

  @Test
  void given_create_when_categoryDoesNotExist_then_throwNotFoundException() {
    var dto = ProductRequestDto.builder()
        .categoryId(CATEGORY_ID)
        .build();

    when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> productService.create(dto));

    verify(productRepository, times(0)).save(any());
    verify(categoryRepository, times(1)).findById(any());
  }

  @Test
  void given_update_when_dataIsValid_then_updateAndReturnMappedProduct() {
    var dto = ProductRequestDto.builder()
        .categoryId(CATEGORY_ID)
        .build();
    var product = loadProduct();
    var category = category();

    when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
    when(categoryRepository.findById(dto.categoryId())).thenReturn(Optional.of(category));

    productService.update(PRODUCT_ID, dto);

    verify(productRepository, times(1)).findById(any());
    verify(categoryRepository, times(1)).findById(any());
    verify(productRepository, times(1)).save(any());
  }

  @Test
  void given_update_when_productDoesNotExist_then_throwNotFoundException() {
    ProductRequestDto dto = ProductRequestDto.builder()
        .name("Atualizando nome produto")
        .build();

    when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> productService.update(PRODUCT_ID, dto));

    verify(productRepository, times(1)).findById(any());
    verify(categoryRepository, times(0)).findById(any());
    verify(productRepository, times(0)).save(any());
  }

  @Test
  void given_update_when_categoryDoesNotExist_then_throwNotFoundException() {
    ProductRequestDto dto = ProductRequestDto.builder()
        .categoryId("outro-id")
        .build();
    var product = loadProduct();

    when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
    when(categoryRepository.findById(dto.categoryId())).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> productService.update(PRODUCT_ID, dto));

    verify(productRepository, times(1)).findById(any());
    verify(categoryRepository, times(1)).findById(any());
    verify(productRepository, times(0)).save(any());
  }

  @Test
  void given_delete_when_productExists_then_deleteSuccessfully() {
    var product = loadProduct();

    when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

    productService.delete(product.getId());

    verify(productRepository, times(1)).findById(product.getId());
    verify(productRepository, times(1)).deleteById(product.getId());
  }
}