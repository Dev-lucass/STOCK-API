package com.example.estoque_api.controller;

import com.example.estoque_api.dto.response.entity.HistoryEntityResponseDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.service.HistoryEntityService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HistoryEntityController.class)
class HistoryEntityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HistoryEntityService service;

    @Test
    @DisplayName("Get all history success")
    void getAllHistory_Success() throws Exception {
        HistoryEntityResponseDTO response = HistoryEntityResponseDTO.builder().build();
        when(service.findAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/history")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("Filter history with parameters success")
    void filterHistory_WithParams_Success() throws Exception {
        HistoryEntityResponseDTO response = HistoryEntityResponseDTO.builder().build();
        Page<HistoryEntityResponseDTO> page = new PageImpl<>(List.of(response));

        when(service.filterHistory(anyString(),any(InventoryAction.class), anyInt(), anyInt(), anyInt(), anyInt(), anyInt()))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/history/filterHistory")
                        .param("nameTool", "makita")
                        .param("InventoryAction", "TAKE")
                        .param("quantity", "10")
                        .param("minQuantity", "5")
                        .param("maxQuantity", "20")
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    @DisplayName("Filter history with empty parameters success")
    void filterHistory_EmptyParams_Success() throws Exception {
        Page<HistoryEntityResponseDTO> page = new PageImpl<>(List.of());

        when(service.filterHistory(null, null, null,null, null, 0, 10))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/history/filterHistory")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0));
    }

    @Test
    @DisplayName("Filter history should return 400 when InventoryAction is invalid")
    void filterHistory_InvalidAction_BadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/history/filterHistory")
                        .param("InventoryAction", "INVALID_ACTION")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}