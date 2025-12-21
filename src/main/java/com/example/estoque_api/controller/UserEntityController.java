package com.example.estoque_api.controller;

import com.example.estoque_api.dto.request.UserEntityDTO;
import com.example.estoque_api.dto.response.UserEntityResponseDTO;
import com.example.estoque_api.mapper.persistenceMapper.UserEntityMapper;
import com.example.estoque_api.mapper.responseMapper.UserEntityResponseMapper;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.service.UserEntityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/user")
public class UserEntityController {

    private final UserEntityService service;
    private final UserEntityMapper mapper;
    private final UserEntityResponseMapper responseMapper;

    public UserEntityController(UserEntityService service, UserEntityMapper mapper, UserEntityResponseMapper responseMapper) {
        this.service = service;
        this.mapper = mapper;
        this.responseMapper = responseMapper;
    }

    @PostMapping
    public ResponseEntity<UserEntityResponseDTO> save(@RequestBody @Valid UserEntityDTO dto) {
        UserEntity mapperEntity = mapper.toEntity(dto);
        UserEntity saved = service.save(mapperEntity);
        UserEntityResponseDTO response = responseMapper.toResponse(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<UserEntityResponseDTO>> findAll() {
        List<UserEntity> all = service.findAll();
        List<UserEntityResponseDTO> responseList = responseMapper.toResponseList(all);
        return ResponseEntity.ok(responseList);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserEntityResponseDTO> update(@PathVariable("userId") Long id, @RequestBody @Valid UserEntityDTO dto) {
        UserEntity mapperEntity = mapper.toEntity(dto);
        UserEntity updated = service.update(id, mapperEntity);
        UserEntityResponseDTO response = responseMapper.toResponse(updated);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable("userId") Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
