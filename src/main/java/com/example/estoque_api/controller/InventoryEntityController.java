package com.example.estoque_api.controller;

import com.example.estoque_api.dto.request.InventoryEntityDTO;
import com.example.estoque_api.dto.request.TakeFromInventory;
import com.example.estoque_api.dto.response.entity.InventoryEntityResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryEntityReturnResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryEntityTakeResponseDTO;
import com.example.estoque_api.service.InventoryEntityService;
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

    private final InventoryEntityService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryEntityResponseDTO save(@RequestBody @Valid InventoryEntityDTO dto) {
        return service.save(dto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryEntityResponseDTO> findAllByToolIsActive() {
        return service.findAllByToolIsActive();
    }

    @PutMapping("/{invenvoryId}")
    @ResponseStatus(HttpStatus.OK)
    public InventoryEntityResponseDTO update(@PathVariable("invenvoryId") Long id, @RequestBody @Valid InventoryEntityDTO dto) {
        return service.update(id, dto);
    }

    @PutMapping("takeFromInventory")
    @ResponseStatus(HttpStatus.OK)
    public InventoryEntityTakeResponseDTO takeFromInventory(@RequestBody @Valid TakeFromInventory takeFromInventory) {
        return service.takeFromInventory(takeFromInventory);
    }

    @PutMapping("returnFromInventory")
    @ResponseStatus(HttpStatus.OK)
    public InventoryEntityReturnResponseDTO returnFromInventory(@RequestBody @Valid TakeFromInventory returneFromInventory) {
        return service.returnFromInventory(returneFromInventory);
    }

    @GetMapping("filterByQuantity")
    @ResponseStatus(HttpStatus.OK)
    public Page<InventoryEntityResponseDTO> filterByQuantity(
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
