package com.example.estoque_api.controller;

import com.example.estoque_api.dto.response.entity.HistoryResponseDTO;
import com.example.estoque_api.service.HistoryEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/history")
public class HistoryEntityController {

    private final HistoryEntityService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public HistoryResponseDTO getAllHistory() {
        return null;
    }
}
