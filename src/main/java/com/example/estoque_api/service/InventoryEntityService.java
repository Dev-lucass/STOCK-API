package com.example.estoque_api.service;

import com.example.estoque_api.dto.internal.HistoryEntityDTO;
import com.example.estoque_api.dto.request.InventoryEntityDTO;
import com.example.estoque_api.dto.request.TakeFromInventory;
import com.example.estoque_api.dto.response.entity.InventoryEntityResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryEntityReturnResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryEntityTakeResponseDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.exceptions.DuplicateResouceException;
import com.example.estoque_api.exceptions.ErrorReturnToInventoryResponseException;
import com.example.estoque_api.exceptions.InvalidQuantityException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.mapper.InventoryEntityMapper;
import com.example.estoque_api.model.HistoryEntity;
import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.model.ProductEntity;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.repository.InventoryEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryEntityService {

    private final InventoryEntityRepository repository;
    private final InventoryEntityMapper mapper;
    private final ProductEntityService productService;
    private final UserEntityService userService;
    private final HistoryEntityService historyService;

    public InventoryEntityResponseDTO save(InventoryEntityDTO dto) {
        var product = productService.findProductByIdOrElseThrow(dto.productId());
        validationInventoryProductIsDuplicatedOnCreate(product);

        var inventoryEntityMapped = mapper
                .toEntityInventory(dto, product);

        var inventorySaved = repository.save(inventoryEntityMapped);
        return mapper.toResponseEntityInventory(inventorySaved);
    }

    public List<InventoryEntityResponseDTO> findAllByProductIsActive() {
        return repository.findAllByProductActiveTrue()
                .stream()
                .map(mapper::toResponseEntityInventory)
                .toList();
    }

    public InventoryEntityResponseDTO update(Long id, InventoryEntityDTO dto) {
        var inventory = findInventoryByIdOrElseThrow(id);

        var product = productService
                .findProductByIdOrElseThrow(dto.productId());

        validateInventoryProductIsDuplicatedOnUpdate(product, id);
        mapper.updateEntity(inventory, dto, product);

        var inventoryUpdated = repository.save(inventory);
        return mapper.toResponseEntityInventory(inventoryUpdated);
    }

    public InventoryEntityTakeResponseDTO takeFromInventory(TakeFromInventory fromInventory) {

        var user = userService
                .findUserByIdOrElseThrow(fromInventory.userId());

        var product = productService
                .findProductByIdOrElseThrow(fromInventory.productId());

        var inventory = findInventoryByProductOrElseThrow(product);

        int quantityUpdated = subtractingQuantity(
                inventory.getQuantity(),
                fromInventory.quantity());

        validationQuantityTakedIsValid(quantityUpdated);

        inventory.setQuantity(quantityUpdated);

        var historyDto = mapper.toHistoryEntityDTO(
                fromInventory.quantity(),
                user,
                product,
                InventoryAction.TAKE);

        historyService.save(historyDto);
        return mapper.toTakeInventoryResponse(inventory, fromInventory.quantity());
    }

    public List<InventoryEntityReturnResponseDTO> returnFromInventory(TakeFromInventory fromInventory) {

        var user = userService
                .findUserByIdOrElseThrow(fromInventory.userId());

        var product = productService
                .findProductByIdOrElseThrow(fromInventory.productId());

        var inventory = findInventoryByProductOrElseThrow(product);

        validationReturnToInventory(
                user,
                product
        );

        validationReturnQuantityIsValid(
                inventory.getQuantity(),
                fromInventory.quantity()
        );

        int quantityUpdated = sumQuantity(
                inventory.getQuantity(),
                fromInventory.quantity());

        inventory.setQuantity(quantityUpdated);

        var historyDto = mapper.toHistoryEntityDTO(
                fromInventory.quantity(),
                user,
                product,
                InventoryAction.RETURN);

        saveHistory(historyDto);
        return mapper.toReturnedInventoryResponse(inventory, fromInventory.quantity());
    }

    private void validationReturnToInventory(UserEntity user, ProductEntity product) {
        var totalTaken = historyService.findByUser(user)
                .stream()
                .filter(h -> h.getProduct().equals(product))
                .filter(h -> h.getAction() == InventoryAction.TAKE)
                .mapToInt(HistoryEntity::getQuantity)
                .sum();

        if (totalTaken == 0)
            throw new ErrorReturnToInventoryResponseException("The user did not pick up this product, it cannot be returned.");
    }

    private InventoryEntity findInventoryByProductOrElseThrow(ProductEntity product) {
        return repository.findByProduct(product)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in inventory"));
    }

    private void saveHistory(HistoryEntityDTO dto) {
        historyService.save(dto);
    }

    private int subtractingQuantity(int quantity, int takeQuantity) {
        return quantity - takeQuantity;
    }

    private int sumQuantity(int quantity, int takeQuantity) {
        return quantity + takeQuantity;
    }

    private void validationReturnQuantityIsValid(int quantityTaked, int quantityReturned) {
        if (quantityReturned > quantityTaked)
            throw new InvalidQuantityException("Quantity returned  is invalid, you took a smaller quantity");
    }

    private void validationQuantityTakedIsValid(int quantity) {
        if (quantity < 0)
            throw new InvalidQuantityException("Quantity sold out");
    }

    private void validationInventoryProductIsDuplicatedOnCreate(ProductEntity product) {
        if (repository.existsByProduct(product))
            throw new DuplicateResouceException("Product already registered in inventory");
    }

    private void validateInventoryProductIsDuplicatedOnUpdate(ProductEntity product, Long id) {
        if (repository.existsByProductAndIdNot(product, id))
            throw new DuplicateResouceException("Product already registered in inventory");
    }

    private InventoryEntity findInventoryByIdOrElseThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid inventory id"));
    }
}
