package com.rafaeldsal.ws.minhaprata.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaeldsal.ws.minhaprata.dto.category.CategoryDto;
import com.rafaeldsal.ws.minhaprata.model.jpa.Category;
import com.rafaeldsal.ws.minhaprata.service.CategoryService;
import com.rafaeldsal.ws.minhaprata.service.TokenService;
import com.rafaeldsal.ws.minhaprata.utils.IdGenerator;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(profiles = "test")
class CategoryControllerTest {

    private final String CATEGORY_ID = IdGenerator.UUIDGenerator("cat");

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;
    @MockitoBean
    private TokenService tokenService;

    @Test
    void given_findAll_then_returnAllCategory() throws Exception {
        List<Category> categories = new ArrayList<>();
        Category category1 = Category.builder().id(IdGenerator.UUIDGenerator("cat"))
                .description("Categoria de teste 1")
                .name("Categoria 1")
                .build();
        categories.add(category1);
        Category category2 = Category.builder().id(IdGenerator.UUIDGenerator("cat"))
                .description("Categoria de teste 2")
                .name("Categoria 2")
                .build();
        categories.add(category2);
        Category category3 = Category.builder().id(IdGenerator.UUIDGenerator("cat"))
                .description("Categoria de teste 3")
                .name("Categoria 3")
                .build();
        categories.add(category3);
        Category category4 = Category.builder().id(IdGenerator.UUIDGenerator("cat"))
                .description("Categoria de teste 4")
                .name("Categoria 4")
                .build();
        categories.add(category4);

        when(categoryService.readAll()).thenReturn(categories);

        mockMvc.perform(get("/category"))
                .andExpect(status().isOk());
    }

    @Test
    void given_findById_when_idExists_then_returnOneCategory() throws Exception {
        Category category = Category.builder()
                .id(CATEGORY_ID)
                .name("Categoria teste")
                .description("Categoria de testes unitários")
                .build();

        when(categoryService.findById(CATEGORY_ID)).thenReturn(category);

        mockMvc.perform(get("/category/{categoryId}", CATEGORY_ID))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(CATEGORY_ID)))
                .andExpect(jsonPath("$.name", Matchers.is("Categoria teste")))
                .andExpect(jsonPath("$.description", Matchers.is("Categoria de testes unitários")));

        verify(categoryService, times(1)).findById(CATEGORY_ID);
    }

    @Test
    void given_create_when_categoryDtoIsValid_then_returnCategoryIsCreated() throws Exception {
        CategoryDto dto = CategoryDto.builder()
                .description("Categoria de teste")
                .name("Nova Categoria")
                .id(null)
                .build();

        Category category = Category.builder()
                .id(CATEGORY_ID)
                .name("Nova Categoria")
                .description("Categoria de teste")
                .build();

        when(categoryService.create(dto)).thenReturn(category);

        mockMvc.perform(post("/category")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.is(CATEGORY_ID)))
                .andExpect(jsonPath("$.name", Matchers.is("Nova Categoria")))
                .andExpect(jsonPath("$.description", Matchers.is("Categoria de teste")));

        verify(categoryService, times(1)).create(dto);
    }

    @Test
    void given_create_when_categoryDtoIsMissingValues_then_returnBadRequest() throws Exception {
        CategoryDto dto = CategoryDto.builder()
                .description("CATE")
                .name("")
                .build();

        mockMvc.perform(post("/category")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is("[name=não pode ser nulo ou vazio, description=deve ter tamanho entre 5 e 255]")))
                .andExpect(jsonPath("$.status", Matchers.is("BAD_REQUEST")))
                .andExpect(jsonPath("$.statusCode", Matchers.is(400)));

        verify(categoryService, times(0)).create(dto);
    }

    @Test
    void given_update_when_categoryDtoIsValid_then_returnCategoryIsUpdated() throws Exception {
        CategoryDto dto = CategoryDto.builder()
                .description("Categoria de teste")
                .name("Nova Categoria")
                .id(CATEGORY_ID)
                .build();

        Category category = Category.builder()
                .id(CATEGORY_ID)
                .name("Nova Categoria")
                .description("Categoria de teste")
                .build();

        when(categoryService.update(CATEGORY_ID, dto)).thenReturn(category);

        mockMvc.perform(put("/category/{id}", CATEGORY_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(CATEGORY_ID)))
                .andExpect(jsonPath("$.name", Matchers.is("Nova Categoria")))
                .andExpect(jsonPath("$.description", Matchers.is("Categoria de teste")));

        verify(categoryService, times(1)).update(CATEGORY_ID, dto);
    }

    @Test
    void given_update_when_categoryDtoIsMissingValues_then_returnBadRequest() throws Exception {
        CategoryDto dto = CategoryDto.builder()
                .description("")
                .name("New")
                .id(CATEGORY_ID)
                .build();

        mockMvc.perform(put("/category/{id}", CATEGORY_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is("[name=deve ter tamanho entre 4 e 15, description=deve ter tamanho entre 5 e 255]")))
                .andExpect(jsonPath("$.status", Matchers.is("BAD_REQUEST")))
                .andExpect(jsonPath("$.statusCode", Matchers.is(400)));

        verify(categoryService, times(0)).update(CATEGORY_ID, dto);
    }

    @Test
    void given_delete_when_idExists_then_noReturnAndNoContent() throws Exception {
        mockMvc.perform(delete("/category/{id}", CATEGORY_ID))
                .andExpect(status().isNoContent());
        verify(categoryService, times(1)).delete(CATEGORY_ID);
    }

}