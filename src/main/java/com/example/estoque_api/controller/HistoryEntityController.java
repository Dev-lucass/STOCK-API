package com.example.estoque_api.controller;

import com.example.estoque_api.dto.response.entity.HistoryResponseDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/history")
public class HistoryEntityController {

    private final HistoryService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<HistoryResponseDTO> getAllHistory() {
        return service.findAll();
    }

    @GetMapping("filterHistory")
    @ResponseStatus(HttpStatus.OK)
    public Page<HistoryResponseDTO> filterHistory(
            @RequestParam(value = "nameTool", required = false) String nameTool,
            @RequestParam(value = "InventoryAction", required = false) InventoryAction action,
            @RequestParam(value = "quantity", required = false) Integer quantity,
            @RequestParam(value = "minQuantity", required = false) Integer minQuantity,
            @RequestParam(value = "maxQuantity", required = false) Integer maxQuantity,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize
    ) {
        return service.filterHistory(
                nameTool,
                action,
                quantity,
                minQuantity,
                maxQuantity,
                pageNumber,
                pageSize);
    }
}
