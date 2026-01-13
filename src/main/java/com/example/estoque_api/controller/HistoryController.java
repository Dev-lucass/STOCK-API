package com.example.estoque_api.controller;

import com.example.estoque_api.dto.request.filter.HistoryFilterDTO;
import com.example.estoque_api.dto.response.filter.HistoryFilterResponseDTO;
import com.example.estoque_api.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/history")
public class HistoryController {

    private final HistoryService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<HistoryFilterResponseDTO> findAll(@ModelAttribute HistoryFilterDTO filter,
                                                  @PageableDefault(sort = "id") Pageable pageable) {
        return service.findAll(filter, pageable);
    }
}
