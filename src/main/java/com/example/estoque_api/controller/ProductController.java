package com.example.estoque_api.controller;

import com.example.estoque_api.dto.request.ProductEntityDTO;
import com.example.estoque_api.dto.response.ProductEntityResponseDTO;
import com.example.estoque_api.mapper.persistenceMapper.ProductEntityMapper;
import com.example.estoque_api.mapper.responseMapper.ProductEntityResponseMapper;
import com.example.estoque_api.model.ProductEntity;
import com.example.estoque_api.service.ProductEntityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/product")
public class ProductController {

    private final ProductEntityService service;
    private final ProductEntityMapper mapper;
    private final ProductEntityResponseMapper responseMapper;

    public ProductController(ProductEntityService service, ProductEntityMapper mapper, ProductEntityResponseMapper responseMapper) {
        this.service = service;
        this.mapper = mapper;
        this.responseMapper = responseMapper;
    }

    @PostMapping
    public ResponseEntity<ProductEntityResponseDTO> save(@RequestBody @Valid ProductEntityDTO dto) {
        ProductEntity mapperEntity = mapper.toEntity(dto);
        ProductEntity saved = service.save(mapperEntity);
        ProductEntityResponseDTO response = responseMapper.toResponse(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductEntityResponseDTO>> findAll() {
        List<ProductEntity> all = service.findAll();
        List<ProductEntityResponseDTO> responseList = responseMapper.toResponseList(all);
        return ResponseEntity.ok(responseList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductEntityResponseDTO> update(@PathVariable("id") Long id, @RequestBody @Valid ProductEntityDTO dto) {
        ProductEntity mapperEntity = mapper.toEntity(dto);
        ProductEntity updated = service.update(id, mapperEntity);
        ProductEntityResponseDTO response = responseMapper.toResponse(updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
