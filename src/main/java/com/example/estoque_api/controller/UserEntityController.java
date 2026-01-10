package com.example.estoque_api.controller;

import com.example.estoque_api.dto.request.filter.UserFilterDTO;
import com.example.estoque_api.dto.request.persist.UserDTO;
import com.example.estoque_api.dto.response.entity.UserResponseDTO;
import com.example.estoque_api.dto.response.filter.UserFilterResponseDTO;
import com.example.estoque_api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user")
public class UserEntityController {

    private final UserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO save(@RequestBody @Valid UserDTO dto) {
        return service.save(dto);
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserResponseDTO update(@PathVariable Long userId, @RequestBody @Valid UserDTO dto) {
        return service.update(userId, dto);
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId) {
        service.disableById(userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<UserFilterResponseDTO> findAll(@ModelAttribute UserFilterDTO filter,
                                               @PageableDefault(sort = "id") Pageable pageable) {
        return service.findAll(filter,pageable);
    }
}
