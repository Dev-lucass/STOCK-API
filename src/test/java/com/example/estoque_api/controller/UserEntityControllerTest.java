package com.example.estoque_api.controller;

import com.example.estoque_api.dto.request.filter.UserFilterDTO;
import com.example.estoque_api.dto.request.persist.UserDTO;
import com.example.estoque_api.dto.response.entity.UserResponseDTO;
import com.example.estoque_api.dto.response.filter.UserFilterResponseDTO;
import com.example.estoque_api.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
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

    @Test
    void save_ValidDto_ReturnsCreated() throws Exception {
        var dto = UserDTO.builder()
                .username("johndoe")
                .cpf("12345678909")
                .address("123 Street Name")
                .build();

        var response = UserResponseDTO.builder()
                .id(1L)
                .username("johndoe")
                .createdAt(LocalDateTime.now())
                .build();

        when(service.save(any(UserDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("johndoe"));
    }

    @Test
    void update_ValidIdAndDto_ReturnsNoContent() throws Exception {
        var dto = UserDTO.builder()
                .username("johnupdated")
                .cpf("12345678909")
                .address("456 Updated Ave")
                .build();

        var response = UserResponseDTO.builder()
                .id(1L)
                .username("johnupdated")
                .createdAt(LocalDateTime.now())
                .build();

        when(service.update(anyLong(), any(UserDTO.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/user/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_ValidId_ReturnsNoContent() throws Exception {
        doNothing().when(service).disableById(anyLong());

        mockMvc.perform(patch("/api/v1/user/{userId}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void findAll_ValidParams_ReturnsPage() throws Exception {
        var response = UserFilterResponseDTO.builder()
                .id(1L)
                .username("johndoe")
                .cpf("12345678909")
                .active(true)
                .address("123 Street Name")
                .build();

        var page = new PageImpl<>(List.of(response));

        when(service.findAll(any(UserFilterDTO.class), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/user")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].username").value("johndoe"))
                .andExpect(jsonPath("$.content[0].cpf").value("12345678909"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }
}