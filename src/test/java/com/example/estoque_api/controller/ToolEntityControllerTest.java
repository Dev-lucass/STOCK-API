package com.example.estoque_api.controller;

import com.example.estoque_api.dto.request.filter.ToolFilterDTO;
import com.example.estoque_api.dto.request.persist.ToolDTO;
import com.example.estoque_api.dto.response.entity.ToolResponseDTO;
import com.example.estoque_api.dto.response.filter.ToolFilterResponseDTO;
import com.example.estoque_api.service.ToolService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@WebMvcTest(ToolEntityController.class)
class ToolEntityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ToolService service;

    @Test
    void save_ValidDto_ReturnsCreated() throws Exception {
        var dto = ToolDTO.builder()
                .name("Hammer")
                .active(true)
                .build();
        var response = ToolResponseDTO.builder()
                .id(1L)
                .name("Hammer")
                .build();

        when(service.save(any(ToolDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/tool")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Hammer"));
    }

    @Test
    void update_ValidIdAndDto_ReturnsNoContent() throws Exception {
        var dto = ToolDTO.builder()
                .name("Updated Hammer")
                .active(true)
                .build();
        var response = ToolResponseDTO.builder()
                .id(1L)
                .build();

        when(service.update(anyLong(), any(ToolDTO.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/tool/{toolId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }

    @Test
    void disable_ValidId_ReturnsNoContent() throws Exception {
        doNothing().when(service).disableById(anyLong());

        mockMvc.perform(patch("/api/v1/tool/{toolId}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void findAll_ValidParams_ReturnsPage() throws Exception {
        var response = ToolFilterResponseDTO.builder()
                .id(1L)
                .name("Screwdriver")
                .build();
        var page = new PageImpl<>(List.of(response));

        when(service.findAll(any(ToolFilterDTO.class), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/tool")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("Screwdriver"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }
}