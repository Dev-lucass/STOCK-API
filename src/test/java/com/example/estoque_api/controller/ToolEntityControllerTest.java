package com.example.estoque_api.controller;

import com.example.estoque_api.dto.request.ToolEntityDTO;
import com.example.estoque_api.dto.response.entity.ToolEntityResponseDTO;
import com.example.estoque_api.model.ToolEntity;
import com.example.estoque_api.service.ToolEntityService;
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

@WebMvcTest(ToolEntityController.class)
class ToolEntityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ToolEntityService service;

    private ToolEntityDTO toolDTO;
    private ToolEntityResponseDTO responseDTO;
    private ToolEntity toolEntity;

    @BeforeEach
    void setUp() {
        toolDTO = new ToolEntityDTO("Teclado Mecânico", true);

        responseDTO = ToolEntityResponseDTO.builder()
                .id(1L)
                .name("Teclado Mecânico")
                .active(true)
                .createdAt(LocalDate.now())
                .build();

        toolEntity = ToolEntity.builder()
                .id(1L)
                .name("Teclado Mecânico")
                .active(true)
                .build();
    }

    @Test
    @DisplayName("Deve salvar um produto e retornar status 201")
    void shouldSaveToolSuccessfully() throws Exception {
        when(service.save(any(ToolEntityDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/tool")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toolDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Teclado Mecânico"));
    }

    @Test
    @DisplayName("Deve retornar todos os produtos ativos")
    void shouldReturnAllActiveTools() throws Exception {
        when(service.findAllIsActive()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/v1/tool"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].active").value(true));
    }

    @Test
    @DisplayName("Deve retornar todos os produtos inativos")
    void shouldReturnAllInactiveTools() throws Exception {
        when(service.findAllIsNotActive()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/v1/tool/findAllIsNotActive"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("Deve atualizar um produto e retornar status 200")
    void shouldUpdateToolSuccessfully() throws Exception {
        when(service.update(eq(1L), any(ToolEntityDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/tool/{idTool}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toolDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Teclado Mecânico"));
    }

    @Test
    @DisplayName("Deve desativar um produto (delete lógico) e retornar 204")
    void shouldDisableToolSuccessfully() throws Exception {
        doNothing().when(service).disableById(1L);

        mockMvc.perform(delete("/api/v1/tool/{idTool}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve filtrar produtos por nome e retornar resultado paginado")
    void shouldFilterToolsByName() throws Exception {
        var page = new PageImpl<>(List.of(toolEntity));
        when(service.filterByNamePageable(anyString(), anyInt(), anyInt())).thenReturn(page);

        mockMvc.perform(get("/api/v1/tool/filterByName")
                        .param("name", "Teclado")
                        .param("pageNumber", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Teclado Mecânico"));
    }
}