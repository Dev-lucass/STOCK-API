package com.example.estoque_api.controller;

import com.example.estoque_api.dto.request.InventoryEntityDTO;
import com.example.estoque_api.dto.request.TakeFromInventory;
import com.example.estoque_api.dto.response.entity.InventoryEntityResponseDTO;
import com.example.estoque_api.service.InventoryEntityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public List<InventoryEntityResponseDTO> findAllByProductIsActive() {
        return service.findAllByProductIsActive();
    }

    @PutMapping("/{invenvoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public InventoryEntityResponseDTO update(@PathVariable("invenvoryId") Long id, @RequestBody @Valid InventoryEntityDTO dto) {
        return service.update(id, dto);
    }

    @PutMapping("takeFromInventory/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public InventoryEntityResponseDTO takeFromInventory(@PathVariable("userId") Long id, @RequestBody @Valid TakeFromInventory takeFromInventory) {
        return service.takeFromInventory(takeFromInventory);
    }

    @PutMapping("returnFromInventory/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public InventoryEntityResponseDTO returnFromInventory(@PathVariable("userId") Long id, @RequestBody @Valid TakeFromInventory returneFromInventory) {
        return service.returnFromInventory(returneFromInventory);
    }
}
