package com.example.estoque_api.controller;

import com.example.estoque_api.dto.request.filter.ToolFilterDTO;
import com.example.estoque_api.dto.request.persist.ToolDTO;
import com.example.estoque_api.dto.response.entity.ToolResponseDTO;
import com.example.estoque_api.dto.response.filter.ToolFilterResponseDTO;
import com.example.estoque_api.service.ToolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{toolId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ToolResponseDTO update(@PathVariable Long toolId, @RequestBody @Valid ToolDTO dto) {
        return service.update(toolId, dto);
    }

    @PatchMapping("/{toolId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disable(@PathVariable Long toolId) {
        service.disableById(toolId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ToolFilterResponseDTO> findAll(@ModelAttribute ToolFilterDTO filter,
                                               @PageableDefault(sort = "id") Pageable pageable) {
        return service.findAll(filter, pageable);
    }
}
