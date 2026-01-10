package com.example.estoque_api.controller;

import com.example.estoque_api.dto.request.filter.UserFilterDTO;
import com.example.estoque_api.dto.request.persist.UserDTO;
import com.example.estoque_api.dto.response.entity.UserResponseDTO;
import com.example.estoque_api.dto.response.filter.UserFilterResponseDTO;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserEntityController.class)
class UserEntityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService service;

    private UserResponseDTO responseDTO;
    private UserDTO requestDTO;
    private UserFilterResponseDTO filterResponse;

    @BeforeEach
    void setUp() {
        filterResponse = UserFilterResponseDTO.builder()
                .id(1L)
                .username("jake")
                .cpf("11144477735")
                .active(true)
                .address("abc street 4th")
                .build();

        requestDTO = new UserDTO("joao_silva", "11144477735", "rua madeireira");

        responseDTO = UserResponseDTO.builder()
                .id(1L)
                .username("joao_silva")
                .build();
    }

    @Test
    @DisplayName("Save user returns 201 created")
    void save_Success() throws Exception {
        when(service.save(any(UserDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("joao_silva"));
    }

    @Test
    @DisplayName("Find all userActive users returns 200 OK")
    void findAll_Success() throws Exception {
        when(service.findAll(any(UserFilterDTO.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(filterResponse)));

        mockMvc.perform(get("/api/v1/user")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @DisplayName("Update user returns 200 OK")
    void update_Success() throws Exception {
        when(service.update(anyLong(), any(UserDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/user/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("Delete user returns 204 no content")
    void delete_Success() throws Exception {
        doNothing().when(service).disableById(1L);

        mockMvc.perform(patch("/api/v1/user/{userId}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Save user with invalid data returns 400 bad request")
    void save_InvalidBody_BadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Find user by id returns 200 OK")
    void findById_Success() throws Exception {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("joao_silva");

        when(service.findUserByIdOrElseThrow(1L)).thenReturn(user);

        mockMvc.perform(get("/api/v1/user/{userId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("joao_silva"));
    }
}