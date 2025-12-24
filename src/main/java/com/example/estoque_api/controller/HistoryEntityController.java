package com.example.estoque_api.controller;

import com.example.estoque_api.dto.response.entity.HistoryEntityResponseDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.model.HistoryEntity;
import com.example.estoque_api.service.HistoryEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/history")
public class HistoryEntityController {

    private final HistoryEntityService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<HistoryEntityResponseDTO> getAllHistory() {
        return service.findAll();
    }

    @GetMapping("filterHistory")
    @ResponseStatus(HttpStatus.OK)
    public Page<HistoryEntityResponseDTO> filterHistory(
            @RequestParam(value = "InventoryAction", required = false) InventoryAction action,
            @RequestParam(value = "quantityInitial", required = false) Integer quantity,
            @RequestParam(value = "minQuantity", required = false) Integer minQuantity,
            @RequestParam(value = "maxQuantity", required = false) Integer maxQuantity,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize
    ) {
        return service.filterHistory(
                action,
                quantity,
                minQuantity,
                maxQuantity,
                pageNumber,
                pageSize);
    }
}
