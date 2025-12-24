package com.example.estoque_api.controller;

import com.example.estoque_api.dto.request.ProductEntityDTO;
import com.example.estoque_api.dto.response.entity.ProductEntityResponseDTO;
import com.example.estoque_api.model.ProductEntity;
import com.example.estoque_api.service.ProductEntityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/product")
public class ProductEntityController {

    private final ProductEntityService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductEntityResponseDTO save(@RequestBody @Valid ProductEntityDTO dto) {
        return service.save(dto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductEntityResponseDTO> findAllIsActive() {
        return service.findAllIsActive();
    }

    @GetMapping("findAllIsNotActive")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductEntityResponseDTO> findAllIsNotActive() {
        return service.findAllIsNotActive();
    }

    @PutMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductEntityResponseDTO update(@PathVariable("productId") Long id, @RequestBody @Valid ProductEntityDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("productId") Long id) {
        service.disableById(id);
    }

    @GetMapping("filterByName")
    @ResponseStatus(HttpStatus.OK)
    public Page<ProductEntity> filterByUsername(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize
    ) {
        return service.filterByNamePageable(
                name,
                pageNumber,
                pageSize
        );
    }
}
