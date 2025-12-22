package com.example.estoque_api.service;

import com.example.estoque_api.dto.internal.HistoryEntityDTO;
import com.example.estoque_api.dto.request.InventoryEntityDTO;
import com.example.estoque_api.dto.request.TakeFromInventory;
import com.example.estoque_api.dto.response.entity.InventoryEntityResponseDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.exceptions.DuplicateResouceException;
import com.example.estoque_api.exceptions.InvalidQuantityException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.mapper.InventoryEntityMapper;
import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.model.ProductEntity;
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
        return mapper.toResponseEntityInventory(inventory);
    }

    public InventoryEntityResponseDTO takeFromInventory(TakeFromInventory fromInventory) {

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
                quantityUpdated,
                user,
                product,
                InventoryAction.TAKE);

        historyService.save(historyDto);
        return mapper.toResponseEntityInventory(inventory);
    }

    public InventoryEntityResponseDTO returnFromInventory(TakeFromInventory fromInventory) {

        var user = userService
                .findUserByIdOrElseThrow(fromInventory.userId());

        var product = productService
                .findProductByIdOrElseThrow(fromInventory.productId());

        var inventory = findInventoryByProductOrElseThrow(product);

        var historyByUser = historyService.findByUser(user);

        int quantityUpdated = sumQuantity(
                inventory.getQuantity(),
                fromInventory.quantity());


        validationReturnQuantityIsValid(
                historyByUser.getQuantity(),
                quantityUpdated
        );

        inventory.setQuantity(quantityUpdated);

        var historyDto = mapper.toHistoryEntityDTO(
                quantityUpdated,
                user,
                product,
                InventoryAction.RETURN);


        saveHistory(historyDto);

        return mapper.toResponseEntityInventory(inventory);
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
            throw new InvalidQuantityException("Quantity taked is invalid, The quantity you want should be less.");
    }

    private void validationInventoryProductIsDuplicatedOnCreate(ProductEntity product) {
        if (repository.existsByProduct(product))
            throw new DuplicateResouceException("Product already registered in inventory");
    }

    private void validateInventoryProductIsDuplicatedOnUpdate(ProductEntity product, Long id) {
        if (repository.existsByProductAndNot(product, id))
            throw new DuplicateResouceException("Product already registered in inventory");
    }

    private InventoryEntity findInventoryByIdOrElseThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid inventory id"));
    }
}
