package com.rafaeldsal.ws.minhaprata.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaeldsal.ws.minhaprata.dto.user.UserDto;
import com.rafaeldsal.ws.minhaprata.dto.user.UserResponseDto;
import com.rafaeldsal.ws.minhaprata.model.enums.UserRole;
import com.rafaeldsal.ws.minhaprata.model.jpa.User;
import com.rafaeldsal.ws.minhaprata.repository.jpa.UserRepository;
import com.rafaeldsal.ws.minhaprata.service.TokenService;
import com.rafaeldsal.ws.minhaprata.service.UserService;
import com.rafaeldsal.ws.minhaprata.utils.IdGenerator;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
@WebMvcTest(UserController.class)
@ActiveProfiles(profiles = "test")
class UserControllerTest {

    private static final String USER_ID = IdGenerator.UUIDGenerator("user");

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TokenService tokenService;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private UserRepository userRepository;

    @Test
    void given_findAll_then_returnAllUsers() throws Exception {
        List<UserResponseDto> users = List.of(
                UserResponseDto.builder()
                        .id(IdGenerator.UUIDGenerator("user"))
                        .name("Carlos Pereira")
                        .email("carlos.pereira@example.com")
                        .cpf("98765432100")
                        .phoneNumber("+55 11 98888-2222")
                        .dtBirth(LocalDate.of(1988, 10, 10))
                        .dtCreated(LocalDateTime.now().minusDays(60))
                        .dtUpdated(LocalDateTime.now())
                        .build(),

                UserResponseDto.builder()
                        .id(IdGenerator.UUIDGenerator("user"))
                        .name("Mariana Silva")
                        .email("mariana.silva@example.com")
                        .cpf("12345678901")
                        .phoneNumber("+55 61 99999-1111")
                        .dtBirth(LocalDate.of(1995, 5, 20))
                        .dtCreated(LocalDateTime.now().minusDays(30))
                        .dtUpdated(LocalDateTime.now())
                        .build(),

                UserResponseDto.builder()
                        .id(IdGenerator.UUIDGenerator("user"))
                        .name("Fernanda Rocha")
                        .email("fernanda.rocha@example.com")
                        .cpf("32165498700")
                        .phoneNumber("+55 21 97777-3333")
                        .dtBirth(LocalDate.of(2000, 1, 15))
                        .dtCreated(LocalDateTime.now().minusDays(15))
                        .dtUpdated(LocalDateTime.now())
                        .build()
        );

        Page<UserResponseDto> pagedUsers = new PageImpl<>(users, PageRequest.of(0, 2), 3);

        when(userService.findAll(0, 10, "ASC", null)).thenReturn(pagedUsers);

        mockMvc.perform(get("/user")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(3));

        Mockito.verify(userService, Mockito.times(1)).findAll(0, 10, "ASC", null);
    }

    @Test
    void given_findById_when_idExists_then_returnUser() throws Exception {
        UserResponseDto user = UserResponseDto.builder()
                .id(USER_ID)
                .name("Carlos Pereira")
                .email("carlos.pereira@example.com")
                .cpf("98765432100")
                .phoneNumber("+55 11 98888-2222")
                .dtBirth(LocalDate.of(1988, 10, 10))
                .dtCreated(LocalDateTime.now().minusDays(60))
                .dtUpdated(LocalDateTime.now())
                .build();

        when(userService.findByID(USER_ID)).thenReturn(user);

        mockMvc.perform(get("/user/{userId}", USER_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(USER_ID))
                .andExpect(jsonPath("$.name").value("Carlos Pereira"))
                .andExpect(jsonPath("$.email").value("carlos.pereira@example.com"))
                .andExpect(jsonPath("$.cpf").value("98765432100"))
                .andExpect(jsonPath("$.phoneNumber").value("+55 11 98888-2222"))
                .andExpect(jsonPath("$.dtBirth").value("1988-10-10"))
                .andExpect(jsonPath("$.dtCreated").exists())
                .andExpect(jsonPath("$.dtUpdated").exists());

        verify(userService, times(1)).findByID(USER_ID);
    }

    @Test
    void given_create_when_userDtoIsValid_then_returnCreatedUser() throws Exception {
        UserDto userRequestDto = UserDto.builder()
                .name("Rafael Souza")
                .email("rafael@email.com")
                .cpf("50292593090")
                .phoneNumber("+55 21 99999-8888")
                .dtBirth(LocalDate.of(1990, 1, 1))
                .password("securePassword123")
                .role(UserRole.USER)
                .build();

        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(USER_ID)
                .name("Rafael Souza")
                .email("rafael@email.com")
                .cpf("50292593090")
                .phoneNumber("+55 21 99999-8888")
                .dtBirth(LocalDate.of(1990, 1, 1))
                .dtCreated(LocalDateTime.now())
                .dtUpdated(LocalDateTime.now())
                .build();

        when(userService.create(userRequestDto)).thenReturn(userResponseDto);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(USER_ID))
                .andExpect(jsonPath("$.name").value(userResponseDto.name()))
                .andExpect(jsonPath("$.email").value(userResponseDto.email()))
                .andExpect(jsonPath("$.cpf").value(userResponseDto.cpf()))
                .andExpect(jsonPath("$.phoneNumber").value(userResponseDto.phoneNumber()))
                .andExpect(jsonPath("$.dtBirth").value(userResponseDto.dtBirth().toString()))
                .andExpect(jsonPath("$.dtCreated").exists())
                .andExpect(jsonPath("$.dtUpdated").exists());

        verify(userService, times(1)).create(userRequestDto);
    }

    @Test
    void given_create_when_userDtoIsInvalid_then_returnBadRequest() throws Exception {
        UserDto userRequestDto = UserDto.builder().build();

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is("[password=atributo obrigatório, phoneNumber=não pode ser nulo ou vazio, name=não pode ser nulo ou vazio]")))
                .andExpect(jsonPath("$.status", Matchers.is("BAD_REQUEST")))
                .andExpect(jsonPath("$.statusCode", Matchers.is(400)));

        verify(userService, times(0)).create(userRequestDto);
    }

    @Test
    void given_update_when_userDtoIsValid_then_returnUserIsUpdated() throws Exception {
        UserDto userDto = UserDto.builder()
                .name("Rafael Souza")
                .email("rafael@email.com")
                .cpf("50292593090")
                .phoneNumber("+55 21 99999-8888")
                .dtBirth(LocalDate.of(1990, 1, 1))
                .password("securePassword123")
                .role(UserRole.USER)
                .build();

        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(USER_ID)
                .name("Rafael Souza")
                .email("rafael@email.com")
                .cpf("50292593090")
                .phoneNumber("+55 21 99999-8888")
                .dtBirth(LocalDate.of(1990, 1, 1))
                .dtCreated(LocalDateTime.now())
                .dtUpdated(LocalDateTime.now())
                .build();

        when(userService.update(USER_ID, userDto)).thenReturn(userResponseDto);

        mockMvc.perform(put("/user/{userId}", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(USER_ID))
                .andExpect(jsonPath("$.name").value(userResponseDto.name()))
                .andExpect(jsonPath("$.email").value(userResponseDto.email()))
                .andExpect(jsonPath("$.cpf").value(userResponseDto.cpf()))
                .andExpect(jsonPath("$.phoneNumber").value(userResponseDto.phoneNumber()))
                .andExpect(jsonPath("$.dtBirth").value(userResponseDto.dtBirth().toString()))
                .andExpect(jsonPath("$.dtCreated").exists())
                .andExpect(jsonPath("$.dtUpdated").exists());

        verify(userService, times(1)).update(USER_ID, userDto);

    }

    @Test
    void given_update_when_userDtoIsNotValid_then_returnBadRequest() throws Exception {
        UserDto userDto = UserDto.builder().build();

        mockMvc.perform(put("/user/{userId}", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is("[password=atributo obrigatório, phoneNumber=não pode ser nulo ou vazio, name=não pode ser nulo ou vazio]")))
                .andExpect(jsonPath("$.status", Matchers.is("BAD_REQUEST")))
                .andExpect(jsonPath("$.statusCode", Matchers.is(400)));


        verify(userService, times(0)).update(USER_ID, userDto);
    }

    @Test
    void given_delete_when_idExists_then_returnNoContent() throws Exception {
        mockMvc.perform(delete("/user/{userId}", USER_ID))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).delete(USER_ID);
    }
}