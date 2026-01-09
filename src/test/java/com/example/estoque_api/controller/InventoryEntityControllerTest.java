package com.example.estoque_api.controller;

import com.example.estoque_api.dto.request.InventoryDTO;
import com.example.estoque_api.dto.request.TakeFromInventory;
import com.example.estoque_api.dto.response.entity.InventoryResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryReturnResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryTakeResponseDTO;
import com.example.estoque_api.service.InventoryService;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InventoryEntityController.class)
class InventoryEntityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private InventoryService service;

    private InventoryResponseDTO responseDTO;
    private InventoryDTO requestDTO;
    private TakeFromInventory takeRequest;
    private long inventoryId;

    @BeforeEach
    void setUp() {
        inventoryId = 1L;
        requestDTO = new InventoryDTO(10, 1L);

        responseDTO = InventoryResponseDTO.builder()
                .id(1L)
                .inventoryId(inventoryId)
                .quantityInitial(10)
                .quantityCurrent(10)
                .toolId(1L)
                .createdAt(LocalDateTime.now())
                .build();

        takeRequest = new TakeFromInventory(1L, inventoryId, 5);
    }

    @Test
    @DisplayName("Save inventory returns 201 created")
    void save_Success() throws Exception {
        when(service.save(any()))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.inventoryId").value(inventoryId));
    }

    @Test
    @DisplayName("Find all active tools returns 200 OK")
    void findAll_Success() throws Exception {
        when(service.findAllByToolIsActive())
                .thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/v1/inventory")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @DisplayName("Update inventory returns 200 OK")
    void update_Success() throws Exception {
        when(service.update(anyLong(), any()))
                .thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/inventory/{invenvoryId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("Take from inventory returns 200 OK")
    void takeFromInventory_Success() throws Exception {
        var takeResponse = InventoryTakeResponseDTO.builder()
                .inventoryId(inventoryId)
                .quantityTaked(5)
                .quantityCurrent(2)
                .quantityInitial(10)
                .usageCount(2)
                .id(1L)
                .toolId(1L)
                .currentLifeCycle(60.0)
                .build();

        when(service.takeFromInventory(any()))
                .thenReturn(takeResponse);

        mockMvc.perform(put("/api/v1/inventory/takeFromInventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(takeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantityTaked").value(5))
                .andExpect(jsonPath("$.quantityCurrent").value(2))
                .andExpect(jsonPath("$.quantityInitial").value(10))
                .andExpect(jsonPath("$.usageCount").value(2));
    }

    @Test
    @DisplayName("Return to inventory returns 200 OK")
    void returnFromInventory_Success() throws Exception {
        var returnResponse = InventoryReturnResponseDTO.builder()
                .inventoryId(inventoryId)
                .quantityReturned(5)
                .quantityInitial(10)
                .quantityCurrent(2)
                .usageCount(1)
                .id(1L)
                .toolId(1L)
                .currentLifeCycle(60.0)
                .build();

        when(service.returnFromInventory(any())).thenReturn(returnResponse);

        mockMvc.perform(put("/api/v1/inventory/returnFromInventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(takeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantityReturned").value(5))
                .andExpect(jsonPath("$.quantityInitial").value(10))
                .andExpect(jsonPath("$.quantityCurrent").value(2))
                .andExpect(jsonPath("$.usageCount").value(1));
    }

    @Test
    @DisplayName("Filter by quantity returns paged data")
    void filterByQuantity_Success() throws Exception {
        var page = new PageImpl<>(List.of(responseDTO));

        when(service.filterByQuantity(anyInt(), anyInt(), anyInt()))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/inventory/filterByQuantity")
                        .param("quantity", "10")
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].inventoryId").value(inventoryId));
    }

    @Test
    @DisplayName("Save inventory with invalid body returns 400 bad request")
    void save_InvalidBody_BadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}