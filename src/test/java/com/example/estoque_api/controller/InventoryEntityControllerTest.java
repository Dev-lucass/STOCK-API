package com.example.estoque_api.controller;

import com.example.estoque_api.dto.request.filter.InventoryFilterDTO;
import com.example.estoque_api.dto.request.persist.InventoryDTO;
import com.example.estoque_api.dto.request.persist.TakeFromInventory;
import com.example.estoque_api.dto.response.entity.InventoryResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryReturnResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryTakeResponseDTO;
import com.example.estoque_api.dto.response.filter.InventoryFilterResponseDTO;
import com.example.estoque_api.service.InventoryService;
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

    @Test
    void save_ValidDto_ReturnsCreated() throws Exception {
        var dto = InventoryDTO.builder()
                .quantity(10)
                .idTool(1L)
                .build();
        var response = InventoryResponseDTO.builder()
                .id(1L)
                .build();

        when(service.save(any(InventoryDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void update_ValidIdAndDto_ReturnsNoContent() throws Exception {
        var dto = InventoryDTO.builder()
                .quantity(20)
                .idTool(1L)
                .build();
        var response = InventoryResponseDTO.builder()
                .id(1L)
                .build();

        when(service.update(anyLong(), any(InventoryDTO.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/inventory/{inventoryId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }

    @Test
    void takeFromInventory_ValidRequest_ReturnsOk() throws Exception {
        var request = TakeFromInventory.builder()
                .userId(1L)
                .inventoryId(1L)
                .quantityTaken(5)
                .build();
        var response = InventoryTakeResponseDTO.builder().build();

        when(service.takeFromInventory(any(TakeFromInventory.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/inventory/takeFromInventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void returnFromInventory_ValidRequest_ReturnsOk() throws Exception {
        var request = TakeFromInventory.builder()
                .userId(1L)
                .inventoryId(1L)
                .quantityTaken(5)
                .build();
        var response = InventoryReturnResponseDTO.builder().build();

        when(service.returnFromInventory(any(TakeFromInventory.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/inventory/returnFromInventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void findAll_ValidParams_ReturnsPage() throws Exception {
        var response = InventoryFilterResponseDTO.builder()
                .id(1L)
                .build();
        var page = new PageImpl<>(List.of(response));

        when(service.findAll(any(InventoryFilterDTO.class), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/inventory")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.totalElements").value(1));
    }
}