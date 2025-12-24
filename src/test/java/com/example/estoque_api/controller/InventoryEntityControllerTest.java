package com.example.estoque_api.controller;

import com.example.estoque_api.dto.request.InventoryEntityDTO;
import com.example.estoque_api.dto.request.TakeFromInventory;
import com.example.estoque_api.dto.response.entity.InventoryEntityResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryEntityReturnResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryEntityTakeResponseDTO;
import com.example.estoque_api.service.InventoryEntityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventoryEntityController.class)
class InventoryEntityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private InventoryEntityService service;

    private InventoryEntityDTO inventoryDTO;
    private InventoryEntityResponseDTO responseDTO;
    private TakeFromInventory takeDTO;

    @BeforeEach
    void setUp() {
        inventoryDTO = new InventoryEntityDTO(100, 1L);
        
        responseDTO = InventoryEntityResponseDTO.builder()
                .id(1L)
                .inventoryId("INV-001")
                .quantityInitial(100)
                .quantityCurrent(100)
                .idTool(1L)
                .build();

        takeDTO = new TakeFromInventory(10L, "INV-001", 10, 10);
    }

    @Test
    @DisplayName("Should create inventory and return status 201")
    void shouldCreateInventory() throws Exception {
        when(service.save(any(InventoryEntityDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventoryDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.inventoryId").value("INV-001"));
    }

    @Test
    @DisplayName("Should return all active inventories")
    void shouldReturnAllActiveInventories() throws Exception {
        when(service.findAllByToolIsActive()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/v1/inventory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @DisplayName("Should update inventory successfully")
    void shouldUpdateInventory() throws Exception {
        when(service.update(eq(1L), any(InventoryEntityDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/inventory/{invenvoryId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("Should take items from inventory")
    void shouldTakeFromInventory() throws Exception {
        InventoryEntityTakeResponseDTO takeResponse = InventoryEntityTakeResponseDTO.builder()
                .inventoryId("INV-001")
                .quantityTaked(10)
                .quantityCurrent(90)
                .build();

        when(service.takeFromInventory(any(TakeFromInventory.class))).thenReturn(takeResponse);

        mockMvc.perform(put("/api/v1/inventory/takeFromInventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(takeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantityTaked").value(10));
    }

    @Test
    @DisplayName("Should return items to inventory")
    void shouldReturnFromInventory() throws Exception {
        InventoryEntityReturnResponseDTO returnResponse = InventoryEntityReturnResponseDTO.builder()
                .inventoryId("INV-001")
                .quantityReturned(10)
                .quantityCurrent(100)
                .build();

        when(service.returnFromInventory(any(TakeFromInventory.class))).thenReturn(returnResponse);

        mockMvc.perform(put("/api/v1/inventory/returnFromInventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(takeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantityReturned").value(10));
    }

    @Test
    @DisplayName("Should filter inventories by quantity and return paged result")
    void shouldFilterByQuantity() throws Exception {
        var page = new PageImpl<>(List.of(responseDTO));
        when(service.filterByQuantity(any(), anyInt(), anyInt())).thenReturn(page);

        mockMvc.perform(get("/api/v1/inventory/filterByQuantity")
                        .param("quantity", "100")
                        .param("pageNumber", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].quantity").value(100));
    }
}