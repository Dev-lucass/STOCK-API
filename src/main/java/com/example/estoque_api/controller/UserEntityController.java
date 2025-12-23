package com.example.estoque_api.controller;

import com.example.estoque_api.dto.request.UserEntityDTO;
import com.example.estoque_api.dto.response.entity.UserEntityResponseDTO;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.service.UserEntityService;
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

    private final UserEntityService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserEntityResponseDTO save(@RequestBody @Valid UserEntityDTO dto) {
        return service.save(dto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserEntityResponseDTO> findAll() {
        return service.findAll();
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserEntityResponseDTO update(@PathVariable("userId") Long id, @RequestBody @Valid UserEntityDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("userId") Long id) {
        service.disableById(id);
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
