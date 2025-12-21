package com.example.estoque_api.controller;

import com.example.estoque_api.dto.response.HistoryResponseDTO;
import com.example.estoque_api.mapper.responseMapper.HistoryEntityMapper;
import com.example.estoque_api.service.HistoryEntityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("api/v1/history")
public class HistoryEntityController {

    private final HistoryEntityService service;
    private final HistoryEntityMapper mapper;

    public HistoryEntityController(HistoryEntityService service, HistoryEntityMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<HistoryResponseDTO> getAllHistory() {
        return service.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

}
