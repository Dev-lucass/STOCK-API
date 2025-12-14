package com.example.estoque_api.controller;

import com.example.estoque_api.dto.request.InventoryDTO;
import com.example.estoque_api.dto.request.TakeFromInventory;
import com.example.estoque_api.dto.response.entity.InventoryResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryReturnResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryTakeResponseDTO;
import com.example.estoque_api.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/inventory")
public class InventoryEntityController {

    private final InventoryService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryResponseDTO save(@RequestBody @Valid InventoryDTO dto) {
        return service.save(dto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponseDTO> findAllByToolIsActive() {
        return service.findAllByToolIsActive();
    }

    @PutMapping("/{invenvoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public InventoryResponseDTO update(@PathVariable Long invenvoryId, @RequestBody @Valid InventoryDTO dto) {
        return service.update(invenvoryId, dto);
    }

    @PutMapping("takeFromInventory")
    @ResponseStatus(HttpStatus.OK)
    public InventoryTakeResponseDTO takeFromInventory(@RequestBody @Valid TakeFromInventory takeFromInventory) {
        return service.takeFromInventory(takeFromInventory);
    }

    @PutMapping("returnFromInventory")
    @ResponseStatus(HttpStatus.OK)
    public InventoryReturnResponseDTO returnFromInventory(@RequestBody @Valid TakeFromInventory returneFromInventory) {
        return service.returnFromInventory(returneFromInventory);
    }

    @GetMapping("filterByQuantity")
    @ResponseStatus(HttpStatus.OK)
    public Page<InventoryResponseDTO> filterByQuantity(
            @RequestParam(value = "quantity", required = false) Integer quantity,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize
    ) {
        return service.filterByQuantity(
                quantity,
                pageNumber,
                pageSize
        );
    }
}
