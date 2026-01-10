package com.example.estoque_api.controller;

import com.example.estoque_api.dto.request.filter.HistoryFilterDTO;
import com.example.estoque_api.dto.response.filter.HistoryFilterResponseDTO;
import com.example.estoque_api.dto.response.filter.ToolFilterResponseDTO;
import com.example.estoque_api.dto.response.filter.UserFilterResponseDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.service.HistoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HistoryEntityController.class)
class HistoryEntityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HistoryService service;

    @Test
    void findAll_ValidParams_ReturnsPageOfHistoryFilterResponseDTO() throws Exception {
        var userResponse = UserFilterResponseDTO.builder().build();
        var toolResponse = ToolFilterResponseDTO.builder().build();

        var responseDTO = HistoryFilterResponseDTO.builder()
                .id(1L)
                .inventoryId(100L)
                .action(InventoryAction.TAKE)
                .user(userResponse)
                .tool(toolResponse)
                .build();

        var page = new PageImpl<>(List.of(responseDTO));

        when(service.findAll(any(HistoryFilterDTO.class), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/history")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].inventoryId").value(100L))
                .andExpect(jsonPath("$.content[0].action").value("TAKE"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void findAll_EmptyResult_ReturnsEmptyPage() throws Exception {
        var page = new PageImpl<HistoryFilterResponseDTO>(List.of());

        when(service.findAll(any(HistoryFilterDTO.class), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.totalElements").value(0));
    }
}