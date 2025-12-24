package com.example.estoque_api.controller;

import com.example.estoque_api.dto.response.entity.HistoryEntityResponseDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.service.HistoryEntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
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

    private HistoryEntityResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        responseDTO = HistoryEntityResponseDTO.builder()
                .historyId(1L)
                .inventoryId("INV-001")
                .userId(10L)
                .productId(5L)
                .quantityTaken(50)
                .action(InventoryAction.TAKE)
                .createdAt(LocalDate.now())
                .build();
    }

    @Test
    @DisplayName("Should return status 200 and a list of history records")
    void shouldReturnAllHistory() throws Exception {
        when(service.findAll()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/v1/history")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].historyId").value(1))
                .andExpect(jsonPath("$[0].inventoryId").value("INV-001"))
                .andExpect(jsonPath("$[0].action").value("TAKE"));
    }

    @Test
    @DisplayName("Should return status 200 and a paginated filter result")
    void shouldReturnFilteredHistory() throws Exception {
        Page<HistoryEntityResponseDTO> page = new PageImpl<>(List.of(responseDTO));

        when(service.filterHistory(any(), any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/history/filterHistory")
                        .param("InventoryAction", "TAKE")
                        .param("quantityInitial", "50")
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].historyId").value(1))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("Should return status 200 even with empty filter parameters")
    void shouldHandleEmptyFilterParameters() throws Exception {
        Page<HistoryEntityResponseDTO> emptyPage = new PageImpl<>(List.of());

        when(service.filterHistory(isNull(), isNull(), isNull(), isNull(), anyInt(), anyInt()))
                .thenReturn(emptyPage);

        mockMvc.perform(get("/api/v1/history/filterHistory")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }
}