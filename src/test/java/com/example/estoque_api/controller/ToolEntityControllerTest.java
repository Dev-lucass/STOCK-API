package com.example.estoque_api.controller;

import com.example.estoque_api.dto.request.persist.ToolDTO;
import com.example.estoque_api.dto.response.entity.ToolResponseDTO;
import com.example.estoque_api.model.ToolEntity;
import com.example.estoque_api.service.ToolService;
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

@WebMvcTest(ToolEntityController.class)
class ToolEntityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ToolService service;

    private ToolResponseDTO responseDTO;
    private ToolDTO requestDTO;
    private ToolEntity tool;

    @BeforeEach
    void setUp() {
        requestDTO = new ToolDTO("Martelo", true);

        responseDTO = ToolResponseDTO.builder()
                .id(1L)
                .name("Martelo")
                .active(true)
                .build();

        tool = ToolEntity.builder()
                .id(1L)
                .name("Martelo")
                .active(true)
                .build();
    }

    @Test
    @DisplayName("Save tool returns 201 created")
    void save_Success() throws Exception {
        when(service.save(any(ToolDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/tool")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.toolName").value("Martelo"));
    }

    @Test
    @DisplayName("Update tool returns 200 OK")
    void update_Success() throws Exception {
        when(service.update(anyLong(), any(ToolDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/tool/{toolId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("Delete (disable) tool returns 204 no content")
    void delete_Success() throws Exception {
        doNothing().when(service).disableById(1L);

        mockMvc.perform(patch("/api/v1/tool/{toolId}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Filter tools by toolName returns paged data")
    void filterByName_Success() throws Exception {
        var page = new PageImpl<>(List.of(tool));

//        when(service.filterByNamePageable(anyString(), anyInt(), anyInt()))
//                .thenReturn(page);

        mockMvc.perform(get("/api/v1/tool/filterByName")
                        .param("toolName", "Martelo")
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].toolName").value("Martelo"));
    }

    @Test
    @DisplayName("Save tool with invalid toolName returns 400 bad request")
    void save_InvalidBody_BadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/tool")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"toolName\": \"\"}"))
                .andExpect(status().isBadRequest());
    }
}