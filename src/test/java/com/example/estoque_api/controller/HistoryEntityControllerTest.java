package com.example.estoque_api.controller;

import com.example.estoque_api.dto.response.entity.HistoryEntityResponseDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.service.HistoryEntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HistoryEntityController.class)
@ExtendWith(MockitoExtension.class)
class HistoryEntityControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private HistoryEntityService service;

    private HistoryEntityResponseDTO history1;
    private HistoryEntityResponseDTO history2;

    @BeforeEach
    void setUp() {
        history1 = HistoryEntityResponseDTO.builder()
                .userId(1L)
                .productId(10L)
                .action(InventoryAction.TAKE)
                .quantity(5)
                .createdAt(LocalDate.now())
                .build();

        history2 = HistoryEntityResponseDTO.builder()
                .userId(2L)
                .productId(11L)
                .action(InventoryAction.RETURN)
                .quantity(3)
                .createdAt(LocalDate.now())
                .build();
    }

    @Test
    void should_return_history_list_successfully() throws Exception {
        when(service.findAll()).thenReturn(List.of(history1, history2));

        mvc.perform(get("/api/v1/history")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(history1.userId()))
                .andExpect(jsonPath("$[0].productId").value(history1.productId()))
                .andExpect(jsonPath("$[0].action").value(history1.action().name()))
                .andExpect(jsonPath("$[0].quantity").value(history1.quantity()))
                .andExpect(jsonPath("$[0].createdAt").value(history1.createdAt().toString()))
                .andExpect(jsonPath("$[1].userId").value(history2.userId()))
                .andExpect(jsonPath("$[1].productId").value(history2.productId()))
                .andExpect(jsonPath("$[1].action").value(history2.action().name()))
                .andExpect(jsonPath("$[1].quantity").value(history2.quantity()))
                .andExpect(jsonPath("$[1].createdAt").value(history2.createdAt().toString()));
    }

    @Test
    void should_return_empty_history_list() throws Exception {
        when(service.findAll()).thenReturn(List.of());

        mvc.perform(get("/api/v1/history")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
