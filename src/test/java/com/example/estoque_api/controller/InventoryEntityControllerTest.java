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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
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
    private InventoryEntityService service;

    private InventoryEntityResponseDTO responseDTO;
    private InventoryEntityDTO requestDTO;
    private TakeFromInventory takeRequest;

    @BeforeEach
    void setUp() {
        requestDTO = new InventoryEntityDTO(10, 1L);

        responseDTO = InventoryEntityResponseDTO.builder()
                .id(1L)
                .inventoryId("INV-001")
                .quantityInitial(10)
                .quantityCurrent(10)
                .idTool(1L)
                .createdAt(LocalDate.now())
                .build();

        takeRequest = new TakeFromInventory(1L, "INV-001", 5);
    }

    @Test
    @DisplayName("Save inventory returns 201 created")
    void save_Success() throws Exception {
        when(service.save(any())).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.inventoryId").value("INV-001"));
    }

    @Test
    @DisplayName("Find all active tools returns 200 OK")
    void findAll_Success() throws Exception {
        when(service.findAllByToolIsActive()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/v1/inventory")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @DisplayName("Update inventory returns 200 OK")
    void update_Success() throws Exception {
        when(service.update(anyLong(), any())).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/inventory/{invenvoryId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("Take from inventory returns 200 OK")
    void takeFromInventory_Success() throws Exception {
        var takeResponse = InventoryEntityTakeResponseDTO.builder()
                .inventoryId("INV-001")
                .quantityTaked(5)
                .build();

        when(service.takeFromInventory(any())).thenReturn(takeResponse);

        mockMvc.perform(put("/api/v1/inventory/takeFromInventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(takeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantityTaked").value(5));
    }

    @Test
    @DisplayName("Return to inventory returns 200 OK")
    void returnFromInventory_Success() throws Exception {
        var returnResponse = InventoryEntityReturnResponseDTO.builder()
                .inventoryId("INV-001")
                .quantityReturned(5)
                .build();

        when(service.returnFromInventory(any())).thenReturn(returnResponse);

        mockMvc.perform(put("/api/v1/inventory/returnFromInventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(takeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantityReturned").value(5));
    }

    @Test
    @DisplayName("Filter by quantity returns paged data")
    void filterByQuantity_Success() throws Exception {
        Page<InventoryEntityResponseDTO> page = new PageImpl<>(List.of(responseDTO));
        when(service.filterByQuantity(anyInt(), anyInt(), anyInt())).thenReturn(page);

        mockMvc.perform(get("/api/v1/inventory/filterByQuantity")
                        .param("quantity", "10")
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].inventoryId").value("INV-001"));
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