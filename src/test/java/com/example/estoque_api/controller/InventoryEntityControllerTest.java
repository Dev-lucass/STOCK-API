package com.example.estoque_api.controller;

import com.example.estoque_api.dto.request.InventoryEntityDTO;
import com.example.estoque_api.dto.response.entity.InventoryEntityResponseDTO;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.service.InventoryEntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InventoryEntityController.class)
@ExtendWith(MockitoExtension.class)
class InventoryEntityControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private InventoryEntityService service;

    private InventoryEntityDTO inventoryDto;
    private InventoryEntityResponseDTO response;

    @BeforeEach
    void setUp() {
        inventoryDto = new InventoryEntityDTO(10, 1L);

        response = InventoryEntityResponseDTO.builder()
                .id(1L)
                .quantity(inventoryDto.quantity())
                .productId(inventoryDto.productId())
                .createdAt(LocalDate.now())
                .build();
    }

    @Test
    void should_save_inventory_successfully() throws Exception {
        when(service.save(any(InventoryEntityDTO.class))).thenReturn(response);

        var json = """
                {
                  "quantity": 10,
                  "productId": 1
                }
                """;

        mvc.perform(post("/api/v1/inventory")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.quantity").value(response.quantity()))
                .andExpect(jsonPath("$.productId").value(response.productId()))
                .andExpect(jsonPath("$.createdAt").value(response.createdAt().toString()));
    }

    @Test
    void should_find_all_active_inventory() throws Exception {
        when(service.findAllByProductIsActive()).thenReturn(List.of(response));

        mvc.perform(get("/api/v1/inventory")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()))
                .andExpect(jsonPath("$[0].quantity").value(response.quantity()))
                .andExpect(jsonPath("$[0].productId").value(response.productId()))
                .andExpect(jsonPath("$[0].createdAt").value(response.createdAt().toString()));
    }

    @Test
    void should_update_inventory_no_content() throws Exception {
        when(service.update(eq(1L), any(InventoryEntityDTO.class)))
                .thenReturn(response);

        var json = """
               {
                 "quantity": 20,
                 "productId": 1
               }
               """;

        mvc.perform(put("/api/v1/inventory/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }


    @Test
    void should_throw_bad_request_when_update_nonexistent_inventory() throws Exception {
        Mockito.doThrow(new ResourceNotFoundException("Invalid inventory id"))
                .when(service).update(eq(40L), any(InventoryEntityDTO.class));

        var json = """
                {
                  "quantity": 20,
                  "productId": 1
                }
                """;

        mvc.perform(put("/api/v1/inventory/40")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Invalid inventory id"))
                .andExpect(jsonPath("$.dateError").value(LocalDate.now().toString()));
    }
}
