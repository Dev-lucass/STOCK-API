package com.example.estoque_api.controller;

import com.example.estoque_api.dto.request.ToolDTO;
import com.example.estoque_api.dto.response.entity.ToolResponseDTO;
import com.example.estoque_api.model.ToolEntity;
import com.example.estoque_api.service.ToolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/tool")
public class ToolEntityController {

    private final ToolService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ToolResponseDTO save(@RequestBody @Valid ToolDTO dto) {
        return service.save(dto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ToolResponseDTO> findAllIsActive() {
        return service.findAllisActive();
    }

    @GetMapping("findAllIsNotActive")
    @ResponseStatus(HttpStatus.OK)
    public List<ToolResponseDTO> findAllIsNotActive() {
        return service.findAllisDisable();
    }

    @PutMapping("/{toolId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ToolResponseDTO update(@PathVariable Long toolId, @RequestBody @Valid ToolDTO dto) {
        return service.update(toolId, dto);
    }

    @PatchMapping("/{toolId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long toolId) {
        service.disableById(toolId);
    }

    @GetMapping("filterByName")
    @ResponseStatus(HttpStatus.OK)
    public Page<ToolEntity> filterByUsername(
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
