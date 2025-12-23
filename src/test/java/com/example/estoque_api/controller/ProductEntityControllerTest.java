package com.example.estoque_api.controller;

import com.example.estoque_api.dto.request.ProductEntityDTO;
import com.example.estoque_api.dto.response.entity.ProductEntityResponseDTO;
import com.example.estoque_api.exceptions.DuplicateResouceException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.service.ProductEntityService;
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
import java.util.Collections;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductEntityController.class)
@ExtendWith(MockitoExtension.class)
class ProductEntityControllerTest {

    @Autowired
   private MockMvc mvc;

    @MockitoBean
    private ProductEntityService service;

    private ProductEntityDTO productDto;
    private ProductEntityResponseDTO response;
    private ProductEntityResponseDTO responseUpdated;

    @BeforeEach
    void setUp() {
        productDto = new ProductEntityDTO("Product A", true);

        response = ProductEntityResponseDTO.builder()
                .id(1L)
                .name(productDto.name())
                .active(productDto.active())
                .createdAt(LocalDate.now())
                .build();

        responseUpdated = ProductEntityResponseDTO.builder()
                .id(1L)
                .name("Product A Updated")
                .active(true)
                .createdAt(LocalDate.now())
                .build();
    }

    @Test
    void should_save_product_successfully() throws Exception {

        when(service.save(any(ProductEntityDTO.class)))
                .thenReturn(response);

        var json = """
                {
                  "name": "Product A",
                  "active": true
                }
                """;

        mvc.perform(post("/api/v1/product")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.name").value(response.name()))
                .andExpect(jsonPath("$.active").value(response.active()))
                .andExpect(jsonPath("$.createdAt").value(response.createdAt().toString()));
    }

    @Test
    void should_throw_bad_request_when_name_invalid() throws Exception {
        var json = """
                {
                  "name": "P",
                  "active": true
                }
                """;

        mvc.perform(post("/api/v1/product")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.dateError").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.invalidFields").isArray());
    }

    @Test
    void should_throw_bad_request_when_active_null() throws Exception {
        var json = """
                {
                  "name": "Product A",
                  "active": null
                }
                """;

        mvc.perform(post("/api/v1/product")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.dateError").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.invalidFields").isArray());
    }

    @Test
    void should_throw_conflict_when_product_duplicate_on_create() throws Exception {
        when(service.save(any(ProductEntityDTO.class)))
                .thenThrow(new DuplicateResouceException("Product already registered"));

        var json = """
                {
                  "name": "Product A",
                  "active": true
                }
                """;

        mvc.perform(post("/api/v1/product")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.CONFLICT.value()))
                .andExpect(jsonPath("$.message").value("Product already registered"))
                .andExpect(jsonPath("$.dateError").value(LocalDate.now().toString()));
    }

    @Test
    void should_find_all_active_products() throws Exception {
        when(service.findAllIsActive())
                .thenReturn(List.of(response));

        mvc.perform(get("/api/v1/product")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()))
                .andExpect(jsonPath("$[0].name").value(response.name()))
                .andExpect(jsonPath("$[0].active").value(response.active()))
                .andExpect(jsonPath("$[0].createdAt").value(response.createdAt().toString()));
    }

    @Test
    void should_return_empty_list_when_no_active_products() throws Exception {
        when(service.findAllIsActive())
                .thenReturn(Collections.emptyList());

        mvc.perform(get("/api/v1/product")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void should_find_all_inactive_products() throws Exception {
        when(service.findAllIsNotActive())
                .thenReturn(List.of(response));

        mvc.perform(get("/api/v1/product/findAllIsNotActive")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()))
                .andExpect(jsonPath("$[0].name").value(response.name()))
                .andExpect(jsonPath("$[0].active").value(response.active()))
                .andExpect(jsonPath("$[0].createdAt").value(response.createdAt().toString()));
    }

    @Test
    void should_return_empty_list_when_no_inactive_products() throws Exception {
        when(service.findAllIsNotActive())
                .thenReturn(Collections.emptyList());

        mvc.perform(get("/api/v1/product/findAllIsNotActive")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void should_update_product_successfully() throws Exception {
        when(service.update(eq(1L), any(ProductEntityDTO.class))).thenReturn(responseUpdated);

        var json = """
                {
                  "name": "Product A Updated",
                  "active": true
                }
                """;

        mvc.perform(put("/api/v1/product/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseUpdated.id()))
                .andExpect(jsonPath("$.name").value(responseUpdated.name()))
                .andExpect(jsonPath("$.active").value(responseUpdated.active()))
                .andExpect(jsonPath("$.createdAt").value(responseUpdated.createdAt().toString()));
    }

    @Test
    void should_throw_bad_request_when_update_nonexistent_product() throws Exception {

        when(service.update(eq(40L), any(ProductEntityDTO.class)))
                .thenThrow(new ResourceNotFoundException("Invalid product id"));

        var json = """
                {
                  "name": "Product A Updated",
                  "active": true
                }
                """;

        mvc.perform(put("/api/v1/product/40")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Invalid product id"))
                .andExpect(jsonPath("$.dateError").value(LocalDate.now().toString()));
    }

    @Test
    void should_throw_conflict_when_update_product_duplicate() throws Exception {

        when(service.update(eq(1L), any(ProductEntityDTO.class)))
                .thenThrow(new DuplicateResouceException("Product already registered"));

        var json = """
                {
                  "name": "Product A",
                  "active": true
                }
                """;

        mvc.perform(put("/api/v1/product/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.CONFLICT.value()))
                .andExpect(jsonPath("$.message").value("Product already registered"))
                .andExpect(jsonPath("$.dateError").value(LocalDate.now().toString()));
    }

    @Test
    void should_delete_product_successfully() throws Exception {
        doNothing().when(service).disableById(1L);

        mvc.perform(delete("/api/v1/product/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void should_throw_bad_request_when_delete_nonexistent_product() throws Exception {

        Mockito.doThrow(new ResourceNotFoundException("Invalid product id"))
                .when(service).disableById(40L);

        mvc.perform(delete("/api/v1/product/40")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Invalid product id"))
                .andExpect(jsonPath("$.dateError").value(LocalDate.now().toString()));
    }
}
