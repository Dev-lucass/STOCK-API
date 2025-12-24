package com.example.estoque_api.controller;

import com.example.estoque_api.dto.request.ToolEntityDTO;
import com.example.estoque_api.dto.response.entity.ToolEntityResponseDTO;
import com.example.estoque_api.model.ToolEntity;
import com.example.estoque_api.service.ToolEntityService;
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

    private final ToolEntityService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ToolEntityResponseDTO save(@RequestBody @Valid ToolEntityDTO dto) {
        return service.save(dto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ToolEntityResponseDTO> findAllIsActive() {
        return service.findAllIsActive();
    }

    @GetMapping("findAllIsNotActive")
    @ResponseStatus(HttpStatus.OK)
    public List<ToolEntityResponseDTO> findAllIsNotActive() {
        return service.findAllIsNotActive();
    }

    @PutMapping("/{idTool}")
    @ResponseStatus(HttpStatus.OK)
    public ToolEntityResponseDTO update(@PathVariable("idTool") Long id, @RequestBody @Valid ToolEntityDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{idTool}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("idTool") Long id) {
        service.disableById(id);
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
