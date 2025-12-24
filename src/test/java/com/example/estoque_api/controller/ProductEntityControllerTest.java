package com.example.estoque_api.controller;

import com.example.estoque_api.dto.request.ProductEntityDTO;
import com.example.estoque_api.dto.response.entity.ProductEntityResponseDTO;
import com.example.estoque_api.model.ProductEntity;
import com.example.estoque_api.service.ProductEntityService;
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

@WebMvcTest(ProductEntityController.class)
class ProductEntityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductEntityService service;

    private ProductEntityDTO productDTO;
    private ProductEntityResponseDTO responseDTO;
    private ProductEntity productEntity;

    @BeforeEach
    void setUp() {
        productDTO = new ProductEntityDTO("Teclado Mecânico", true);

        responseDTO = ProductEntityResponseDTO.builder()
                .id(1L)
                .name("Teclado Mecânico")
                .active(true)
                .createdAt(LocalDate.now())
                .build();

        productEntity = ProductEntity.builder()
                .id(1L)
                .name("Teclado Mecânico")
                .active(true)
                .build();
    }

    @Test
    @DisplayName("Deve salvar um produto e retornar status 201")
    void shouldSaveProductSuccessfully() throws Exception {
        when(service.save(any(ProductEntityDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Teclado Mecânico"));
    }

    @Test
    @DisplayName("Deve retornar todos os produtos ativos")
    void shouldReturnAllActiveProducts() throws Exception {
        when(service.findAllIsActive()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/v1/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].active").value(true));
    }

    @Test
    @DisplayName("Deve retornar todos os produtos inativos")
    void shouldReturnAllInactiveProducts() throws Exception {
        when(service.findAllIsNotActive()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/v1/product/findAllIsNotActive"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("Deve atualizar um produto e retornar status 200")
    void shouldUpdateProductSuccessfully() throws Exception {
        when(service.update(eq(1L), any(ProductEntityDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/product/{productId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Teclado Mecânico"));
    }

    @Test
    @DisplayName("Deve desativar um produto (delete lógico) e retornar 204")
    void shouldDisableProductSuccessfully() throws Exception {
        doNothing().when(service).disableById(1L);

        mockMvc.perform(delete("/api/v1/product/{productId}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve filtrar produtos por nome e retornar resultado paginado")
    void shouldFilterProductsByName() throws Exception {
        var page = new PageImpl<>(List.of(productEntity));
        when(service.filterByNamePageable(anyString(), anyInt(), anyInt())).thenReturn(page);

        mockMvc.perform(get("/api/v1/product/filterByName")
                        .param("name", "Teclado")
                        .param("pageNumber", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Teclado Mecânico"));
    }
}