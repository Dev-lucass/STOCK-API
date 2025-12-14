package com.example.estoque_api.controller;

import com.example.estoque_api.dto.request.InventoryEntityDTO;
import com.example.estoque_api.dto.response.InventoryEntityResponseDTO;
import com.example.estoque_api.mapper.persistenceMapper.InventoryEntityMapper;
import com.example.estoque_api.mapper.responseMapper.InventoryEntityResponseMapper;
import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.service.InventoryEntityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/inventory")
public class InventoryController {

    private final InventoryEntityService service;
    private final InventoryEntityMapper mapper;
    private final InventoryEntityResponseMapper responseMapper;

    public InventoryController(InventoryEntityService service, InventoryEntityMapper mapper, InventoryEntityResponseMapper responseMapper) {
        this.service = service;
        this.mapper = mapper;
        this.responseMapper = responseMapper;
    }

    @PostMapping
    public ResponseEntity<InventoryEntityResponseDTO> save(@RequestBody @Valid InventoryEntityDTO dto) {
        InventoryEntity mapperEntity = mapper.toEntity(dto);
        InventoryEntity saved = service.save(mapperEntity);
        InventoryEntityResponseDTO response = responseMapper.toResponse(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<InventoryEntityResponseDTO>> findAll() {
        List<InventoryEntity> all = service.findAll();
        List<InventoryEntityResponseDTO> responseList = responseMapper.toResponseList(all);
        return ResponseEntity.ok(responseList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryEntityResponseDTO> update(@PathVariable("id") Long id, @RequestBody @Valid InventoryEntityDTO dto) {
        InventoryEntity mapperEntity = mapper.toEntity(dto);
        InventoryEntity updated = service.update(id, mapperEntity);
        InventoryEntityResponseDTO response = responseMapper.toResponse(updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
