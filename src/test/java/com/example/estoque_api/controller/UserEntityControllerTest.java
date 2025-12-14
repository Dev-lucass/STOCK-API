package com.example.estoque_api.controller;

import com.example.estoque_api.dto.request.UserDTO;
import com.example.estoque_api.dto.response.entity.UserResponseDTO;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.service.UserService;
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
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
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
    private UserEntity user;

    @BeforeEach
    void setUp() {
        requestDTO = new UserDTO("joao_silva", "11144477735", "rua madeireira");

        responseDTO = UserResponseDTO.builder()
                .id(1L)
                .username("joao_silva")
                .build();

        user = UserEntity.builder()
                .id(1L)
                .username("joao_silva")
                .active(true)
                .build();
    }

    @Test
    @DisplayName("Save user returns 201 created")
    void save_Success() throws Exception {
        when(service.save(any(UserDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("joao_silva"));
    }

    @Test
    @DisplayName("Find all active users returns 200 OK")
    void findAll_Success() throws Exception {
        when(service.findAll())
                .thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/v1/user")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].username").value("joao_silva"));
    }

    @Test
    @DisplayName("Update user returns 200 OK")
    void update_Success() throws Exception {
        when(service.update(anyLong(), any(UserDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/user/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNoContent())
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
    @DisplayName("Filter users by username returns paged data")
    void filterByUsername_Success() throws Exception {
        var page = new PageImpl<>(List.of(user));

        when(service.filterByUsernamePageable(anyString(), anyInt(), anyInt()))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/user/filterByUsername")
                        .param("username", "joao")
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].username").value("joao_silva"));
    }

    @Test
    @DisplayName("Save user with invalid data returns 400 bad request")
    void save_InvalidBody_BadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"\"}"))
                .andExpect(status().isBadRequest());
    }
}