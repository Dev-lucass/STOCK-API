package com.example.estoque_api.controller;

import com.example.estoque_api.dto.request.UserEntityDTO;
import com.example.estoque_api.dto.response.entity.UserEntityResponseDTO;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.service.UserEntityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserEntityController.class)
class UserEntityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserEntityService service;

    private UserEntityDTO userDTO;
    private UserEntityResponseDTO responseDTO;
    private UserEntity userEntity;
    private final String VALID_CPF = "11144477735";

    @BeforeEach
    void setUp() {
        userDTO = new UserEntityDTO("john_doe", VALID_CPF, "Rua Teste, 123");

        responseDTO = UserEntityResponseDTO.builder()
                .id(1L)
                .username("john_doe")
                .createdAt(LocalDate.now())
                .build();

        userEntity = UserEntity.builder()
                .id(1L)
                .username("john_doe")
                .cpf(VALID_CPF)
                .active(true)
                .build();
    }

    @Test
    @DisplayName("Deve salvar um usuário e retornar status 201")
    void shouldSaveUserSuccessfully() throws Exception {
        when(service.save(any(UserEntityDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("john_doe"))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("Deve retornar lista de todos os usuários ativos")
    void shouldReturnAllUsers() throws Exception {
        when(service.findAll()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/v1/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("john_doe"))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("Deve atualizar um usuário e retornar status 200")
    void shouldUpdateUserSuccessfully() throws Exception {
        when(service.update(eq(1L), any(UserEntityDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/user/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john_doe"));
    }

    @Test
    @DisplayName("Deve desativar um usuário e retornar 204")
    void shouldDisableUserSuccessfully() throws Exception {
        doNothing().when(service).disableById(1L);

        mockMvc.perform(delete("/api/v1/user/{userId}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve filtrar usuários por username e retornar página")
    void shouldFilterUsersByUsername() throws Exception {
        var page = new PageImpl<>(List.of(userEntity));
        when(service.filterByUsernamePageable(anyString(), anyInt(), anyInt())).thenReturn(page);

        mockMvc.perform(get("/api/v1/user/filterByUsername")
                        .param("username", "john")
                        .param("pageNumber", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].username").value("john_doe"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }
}