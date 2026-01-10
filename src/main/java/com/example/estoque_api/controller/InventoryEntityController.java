package com.example.estoque_api.controller;

import com.example.estoque_api.dto.request.filter.InventoryFilterDTO;
import com.example.estoque_api.dto.request.persist.InventoryDTO;
import com.example.estoque_api.dto.request.persist.TakeFromInventory;
import com.example.estoque_api.dto.response.entity.InventoryResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryReturnResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryTakeResponseDTO;
import com.example.estoque_api.dto.response.filter.InventoryFilterResponseDTO;
import com.example.estoque_api.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<InventoryFilterResponseDTO> findAll(@ModelAttribute InventoryFilterDTO filter,
                                                    @PageableDefault(sort = "id") Pageable pageable) {
        return service.findAll(filter, pageable);
    }
}
