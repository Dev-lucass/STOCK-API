package com.example.estoque_api.controller;

import com.example.estoque_api.dto.request.UserDTO;
import com.example.estoque_api.dto.response.entity.UserResponseDTO;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponseDTO> findAll() {
        return service.findAll();
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

    @GetMapping("filterByUsername")
    @ResponseStatus(HttpStatus.OK)
    public Page<UserEntity> filterByUsername(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize
    ) {
        return service.filterByUsernamePageable(
                username,
                pageNumber,
                pageSize
        );
    }
}
