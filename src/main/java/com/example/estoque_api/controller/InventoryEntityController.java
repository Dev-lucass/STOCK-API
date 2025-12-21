package com.example.estoque_api.controller;

import com.example.estoque_api.dto.request.InventoryEntityDTO;
import com.example.estoque_api.dto.response.InventoryEntityResponseDTO;
import com.example.estoque_api.mapper.persistenceMapper.InventoryEntityMapper;
import com.example.estoque_api.mapper.responseMapper.InventoryEntityResponseMapper;
import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.model.ProductEntity;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.service.InventoryEntityService;
import com.example.estoque_api.validation.ProductEntityValidation;
import com.example.estoque_api.validation.UserEntityValidation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/inventory")
public class InventoryEntityController {

    private final InventoryEntityService service;
    private final InventoryEntityMapper mapper;
    private final InventoryEntityResponseMapper responseMapper;
    private final ProductEntityValidation validationProductId;
    private final UserEntityValidation userValidation;

    public InventoryEntityController(InventoryEntityService service, InventoryEntityMapper mapper, InventoryEntityResponseMapper responseMapper, ProductEntityValidation validationProductId, UserEntityValidation userValidation) {
        this.service = service;
        this.mapper = mapper;
        this.responseMapper = responseMapper;
        this.validationProductId = validationProductId;
        this.userValidation = userValidation;
    }

    @PostMapping
    public ResponseEntity<InventoryEntityResponseDTO> save(@RequestBody @Valid InventoryEntityDTO dto) {
        ProductEntity product = validationProductId.validationProductEntityIdIsValid(dto.productId());
        InventoryEntity mapperEntity = mapper.toEntity(dto);
        mapperEntity.setProduct(product);
        InventoryEntity saved = service.save(mapperEntity);
        InventoryEntityResponseDTO response = responseMapper.toResponse(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<InventoryEntityResponseDTO>> findAll() {
        List<InventoryEntity> all = service.findAll();
        List<InventoryEntityResponseDTO> responseList = responseMapper.toResponseList(all);
        return ResponseEntity.ok(responseList);
    }

    @PutMapping("/{invenvoryId}")
    public ResponseEntity<InventoryEntityResponseDTO> update(@PathVariable("invenvoryId") Long id, @RequestBody @Valid InventoryEntityDTO dto) {
        ProductEntity product = validationProductId.validationProductEntityIdIsValid(dto.productId());
        InventoryEntity mapperEntity = mapper.toEntity(dto);
        mapperEntity.setProduct(product);
        InventoryEntity updated = service.update(id, mapperEntity);
        InventoryEntityResponseDTO response = responseMapper.toResponse(updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{invenvoryId}")
    public ResponseEntity<Void> delete(@PathVariable("invenvoryId") Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("takeFromInventory/{userId}")
    public ResponseEntity<InventoryEntityResponseDTO> takeFromInventory(@PathVariable("userId") Long id, @RequestBody @Valid InventoryEntityDTO dto) {

        ProductEntity product = validationProductId.validationProductEntityIdIsValid(dto.productId());
        UserEntity user = userValidation.validationUserEntityIdIsValid(id);

        InventoryEntity mapperEntity = mapper.toEntity(dto);
        mapperEntity.setProduct(product);

        InventoryEntity takeFromInventory = service.takeFromInventory(user, mapperEntity);
        InventoryEntityResponseDTO response = responseMapper.toResponse(takeFromInventory);

        return ResponseEntity.ok(response);
    }

    @PutMapping("returnFromInventory/{userId}")
    public ResponseEntity<InventoryEntityResponseDTO> returnFromInventory(@PathVariable("userId") Long id, @RequestBody @Valid InventoryEntityDTO dto) {

        ProductEntity product = validationProductId.validationProductEntityIdIsValid(dto.productId());
        UserEntity user = userValidation.validationUserEntityIdIsValid(id);

        InventoryEntity mapperEntity = mapper.toEntity(dto);
        mapperEntity.setProduct(product);


        InventoryEntity returnFromInventory = service.returnFromInventory(user, mapperEntity);
        InventoryEntityResponseDTO response = responseMapper.toResponse(returnFromInventory);
        return ResponseEntity.ok(response);
    }

}
