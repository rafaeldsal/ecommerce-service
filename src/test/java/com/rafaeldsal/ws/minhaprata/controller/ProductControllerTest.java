package com.rafaeldsal.ws.minhaprata.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaeldsal.ws.minhaprata.dto.category.CategoryDto;
import com.rafaeldsal.ws.minhaprata.dto.product.ProductRequestDto;
import com.rafaeldsal.ws.minhaprata.dto.product.ProductResponseDto;
import com.rafaeldsal.ws.minhaprata.model.jpa.Category;
import com.rafaeldsal.ws.minhaprata.service.ProductService;
import com.rafaeldsal.ws.minhaprata.service.TokenService;
import com.rafaeldsal.ws.minhaprata.utils.IdGenerator;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(profiles = "test")
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    private static final String PRODUCT_ID = IdGenerator.UUIDGenerator("prod");
    private static final String CATEGORY_ID = IdGenerator.UUIDGenerator("cat");
    private static final LocalDateTime DATE_TIME = LocalDateTime.now();

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TokenService tokenService;
    @MockitoBean
    private ProductService productService;

    @Test
    void given_findAll_then_returnAllProducts() throws Exception {
        CategoryDto category1 = CategoryDto.builder().id(IdGenerator.UUIDGenerator("cat"))
                .description("Categoria de teste 1")
                .name("Categoria 1")
                .build();

        CategoryDto category2 = CategoryDto.builder().id(IdGenerator.UUIDGenerator("cat"))
                .description("Categoria de teste 2")
                .name("Categoria 2")
                .build();

        List<ProductResponseDto> products = List.of(
                ProductResponseDto.builder()
                        .id(IdGenerator.UUIDGenerator("prod"))
                        .name("Silver Ring")
                        .description("Elegant 925 silver ring with cubic zirconia.")
                        .price(BigDecimal.valueOf(199.00))
                        .imgUrl("https://example.com/img/ring1.jpg")
                        .stockQuantity(10L)
                        .dtCreated(DATE_TIME.minusDays(10))
                        .dtUpdated(DATE_TIME)
                        .category(category1)
                        .build(),

                ProductResponseDto.builder()
                        .id(IdGenerator.UUIDGenerator("prod"))
                        .name("Heart Pendant Necklace")
                        .description("925 silver necklace with a heart-shaped pendant.")
                        .price(BigDecimal.valueOf(229.00))
                        .imgUrl("https://example.com/img/necklace1.jpg")
                        .stockQuantity(15L)
                        .dtCreated(DATE_TIME.minusDays(7))
                        .dtUpdated(DATE_TIME)
                        .category(category2)
                        .build()
        );
        Page<ProductResponseDto> pagedProducts = new PageImpl<>(products, PageRequest.of(0, 2), 2);

        when(productService.findAll(0, 10, "ASC", null)).thenReturn(pagedProducts);

        mockMvc.perform(get("/product")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(products.get(0).id()))
                .andExpect(jsonPath("$.content[0].name").value(products.get(0).name()))
                .andExpect(jsonPath("$.content[0].description").value(products.get(0).description()))
                .andExpect(jsonPath("$.content[0].price").value(products.get(0).price()))
                .andExpect(jsonPath("$.content[0].imgUrl").value(products.get(0).imgUrl()))
                .andExpect(jsonPath("$.content[0].stockQuantity").value(products.get(0).stockQuantity()))
                .andExpect(jsonPath("$.content[0].dtCreated").exists())
                .andExpect(jsonPath("$.content[0].dtUpdated").exists())
                .andExpect(jsonPath("$.content[0].category.id").value(products.get(0).category().id()))
                .andExpect(jsonPath("$.content[0].category.name").value(products.get(0).category().name()))
                .andExpect(jsonPath("$.content[0].category.description").value(products.get(0).category().description()))
                .andExpect(jsonPath("$.content[1].id").value(products.get(1).id()))
                .andExpect(jsonPath("$.content[1].name").value(products.get(1).name()))
                .andExpect(jsonPath("$.content[1].description").value(products.get(1).description()))
                .andExpect(jsonPath("$.content[1].price").value(products.get(1).price()))
                .andExpect(jsonPath("$.content[1].imgUrl").value(products.get(1).imgUrl()))
                .andExpect(jsonPath("$.content[1].stockQuantity").value(products.get(1).stockQuantity()))
                .andExpect(jsonPath("$.content[1].dtCreated").exists())
                .andExpect(jsonPath("$.content[1].dtUpdated").exists())
                .andExpect(jsonPath("$.content[1].category.id").value(products.get(1).category().id()))
                .andExpect(jsonPath("$.content[1].category.name").value(products.get(1).category().name()))
                .andExpect(jsonPath("$.content[1].category.description").value(products.get(1).category().description()));

        verify(productService, times(1)).findAll(0, 10, "ASC", null);
    }

    @Test
    void given_findById_when_idExists_then_returnOneProduct() throws Exception {
        CategoryDto c1 = CategoryDto.builder()
                .id(CATEGORY_ID)
                .name("Rings")
                .build();

        ProductResponseDto p1 = ProductResponseDto.builder()
                .id(PRODUCT_ID)
                .name("Heart Pendant Necklace")
                .description("925 silver necklace with a heart-shaped pendant.")
                .price(BigDecimal.valueOf(229.00))
                .imgUrl("https://example.com/img/necklace1.jpg")
                .stockQuantity(15L)
                .dtCreated(DATE_TIME.minusDays(7))
                .dtUpdated(DATE_TIME)
                .category(c1)
                .build();

        when(productService.findById(PRODUCT_ID)).thenReturn(p1);

        mockMvc.perform(get("/product/{productId}", PRODUCT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(p1.id()))
                .andExpect(jsonPath("$.name").value(p1.name()))
                .andExpect(jsonPath("$.description").value(p1.description()))
                .andExpect(jsonPath("$.price").value(p1.price()))
                .andExpect(jsonPath("$.imgUrl").value(p1.imgUrl()))
                .andExpect(jsonPath("$.stockQuantity").value(p1.stockQuantity()))
                .andExpect(jsonPath("$.dtCreated").exists())
                .andExpect(jsonPath("$.dtUpdated").exists())
                .andExpect(jsonPath("$.category.id").value(p1.category().id()))
                .andExpect(jsonPath("$.category.name").value(p1.category().name()));

        verify(productService, times(1)).findById(PRODUCT_ID);
    }

    @Test
    void given_create_when_productDtoIsValid_then_returnProductIsCreated() throws Exception {
        var productRequestDto = loadProductRequestDto();
        CategoryDto c1 = CategoryDto.builder()
                .id(CATEGORY_ID)
                .name("Rings")
                .build();
        var productResponseDto = ProductResponseDto.builder()
                .id(PRODUCT_ID)
                .category(c1)
                .name(productRequestDto.name())
                .description(productRequestDto.description())
                .price(productRequestDto.price())
                .imgUrl(productRequestDto.imgUrl())
                .stockQuantity(productRequestDto.stockQuantity())
                .dtCreated(DATE_TIME.minusDays(7))
                .dtUpdated(DATE_TIME)
                .build();

        when(productService.create(productRequestDto)).thenReturn(productResponseDto);

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(productRequestDto)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(productResponseDto.id()))
                .andExpect(jsonPath("$.name").value(productResponseDto.name()))
                .andExpect(jsonPath("$.description").value(productResponseDto.description()))
                .andExpect(jsonPath("$.price").value(productResponseDto.price()))
                .andExpect(jsonPath("$.imgUrl").value(productResponseDto.imgUrl()))
                .andExpect(jsonPath("$.stockQuantity").value(productResponseDto.stockQuantity()))
                .andExpect(jsonPath("$.dtCreated").exists())
                .andExpect(jsonPath("$.dtUpdated").exists())
                .andExpect(jsonPath("$.category.id").value(productResponseDto.category().id()))
                .andExpect(jsonPath("$.category.name").value(productResponseDto.category().name()))
                .andExpect(jsonPath("$.category.description").value(productResponseDto.category().description()));
    }

    @Test
    void given_create_when_productDtoIsMissingValues_then_returnBadRequest() throws Exception {
        ProductRequestDto dto = ProductRequestDto.builder().build();

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is("[price=deve ser informado, name=não pode ser nulo ou vazio, description=não pode ser nulo ou vazio, categoryId=deve ser informado]")))
                .andExpect(jsonPath("$.status", Matchers.is("BAD_REQUEST")))
                .andExpect(jsonPath("$.statusCode", Matchers.is(400)));

        verify(productService, times(0)).create(dto);
    }

    @Test
    void given_update_when_productDtoIsValid_then_returnProductIsUpdated() throws Exception {
        var productRequestDto = loadProductRequestDto();
        CategoryDto c1 = CategoryDto.builder()
                .id(CATEGORY_ID)
                .name("Rings")
                .build();
        var productResponseDto = ProductResponseDto.builder()
                .id(PRODUCT_ID)
                .category(c1)
                .name(productRequestDto.name())
                .description(productRequestDto.description())
                .price(productRequestDto.price())
                .imgUrl(productRequestDto.imgUrl())
                .stockQuantity(productRequestDto.stockQuantity())
                .dtCreated(DATE_TIME.minusDays(7))
                .dtUpdated(DATE_TIME)
                .build();

        when(productService.update(PRODUCT_ID, productRequestDto)).thenReturn(productResponseDto);

        mockMvc.perform(put("/product/{productId}", PRODUCT_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(productRequestDto)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productResponseDto.id()))
                .andExpect(jsonPath("$.name").value(productResponseDto.name()))
                .andExpect(jsonPath("$.description").value(productResponseDto.description()))
                .andExpect(jsonPath("$.price").value(productResponseDto.price()))
                .andExpect(jsonPath("$.imgUrl").value(productResponseDto.imgUrl()))
                .andExpect(jsonPath("$.stockQuantity").value(productResponseDto.stockQuantity()))
                .andExpect(jsonPath("$.dtCreated").exists())
                .andExpect(jsonPath("$.dtUpdated").exists())
                .andExpect(jsonPath("$.category.id").value(productResponseDto.category().id()))
                .andExpect(jsonPath("$.category.name").value(productResponseDto.category().name()))
                .andExpect(jsonPath("$.category.description").value(productResponseDto.category().description()));
    }

    @Test
    void given_update_when_productDtoIsMissingValues_then_returnBadRequest() throws Exception {
        ProductRequestDto dto = ProductRequestDto.builder().build();

        mockMvc.perform(put("/product/{productId}", PRODUCT_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is("[price=deve ser informado, name=não pode ser nulo ou vazio, description=não pode ser nulo ou vazio, categoryId=deve ser informado]")))
                .andExpect(jsonPath("$.status", Matchers.is("BAD_REQUEST")))
                .andExpect(jsonPath("$.statusCode", Matchers.is(400)));

        verify(productService, times(0)).update(PRODUCT_ID, dto);
    }

    @Test
    void given_delete_when_idExists_then_returnNoContent() throws Exception {
        mockMvc.perform(delete("/product/{productId}", PRODUCT_ID))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).delete(PRODUCT_ID);
    }

    private ProductRequestDto loadProductRequestDto() {
        Category category = Category.builder()
                .id(CATEGORY_ID)
                .name("Rings")
                .description("Category for rings")
                .build();

        return ProductRequestDto.builder()
                .name("Silver Ring")
                .description("Elegant 925 silver ring with cubic zirconia.")
                .price(BigDecimal.valueOf(199.00))
                .imgUrl("https://example.com/img/ring1.jpg")
                .stockQuantity(10L)
                .categoryId(category.getId())
                .build();
    }
}