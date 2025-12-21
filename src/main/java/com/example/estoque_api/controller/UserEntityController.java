package com.example.estoque_api.controller;

import com.example.estoque_api.dto.request.UserEntityDTO;
import com.example.estoque_api.dto.response.UserEntityResponseDTO;
import com.example.estoque_api.service.UserEntityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user")
public class UserEntityController {

    private final UserEntityService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserEntityResponseDTO save(@RequestBody @Valid UserEntityDTO dto) {
        return null;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserEntityResponseDTO> findAll() {
        return null;
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserEntityResponseDTO update(@PathVariable("userId") Long id, @RequestBody @Valid UserEntityDTO dto) {
        return null;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Void delete(@PathVariable("userId") Long id) {
        return null;
    }

}
