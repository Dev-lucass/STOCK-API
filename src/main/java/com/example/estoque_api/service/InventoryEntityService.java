package com.example.estoque_api.service;

import com.example.estoque_api.dto.internal.HistoryEntityDTO;
import com.example.estoque_api.dto.request.InventoryEntityDTO;
import com.example.estoque_api.dto.request.TakeFromInventory;
import com.example.estoque_api.dto.response.entity.InventoryEntityResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryEntityReturnResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryEntityTakeResponseDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.exceptions.*;
import com.example.estoque_api.mapper.InventoryEntityMapper;
import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.model.ProductEntity;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.repository.InventoryEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import static com.example.estoque_api.repository.specs.InventoryEntitySpec.equalsQuantity;

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

        inventoryEntityMapped.setInventoryId(UUID.randomUUID().toString());

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

        var inventory = findByInventoryId(fromInventory.inventoryId());
        var user = findByUser(fromInventory.userId());

        validateQuantityTaken(fromInventory.quantityTaken(),
                                                 inventory.getQuantityCurrent());


        int subtracted = subtractQuantity(inventory.getQuantityCurrent(),
                                                                    fromInventory.quantityTaken());

        inventory.setQuantityCurrent(subtracted);
        repository.save(inventory);

        var history = mapper.buildHistoryDto(
                fromInventory.quantityTaken(),
                InventoryAction.TAKE,
                inventory.getProduct(),
                user,
                fromInventory.inventoryId()
        );

        saveHistory(history);
        return mapper.toTakeInventoryResponse(inventory, fromInventory.quantityTaken());
    }

    public InventoryEntityReturnResponseDTO returnFromInventory(TakeFromInventory fromInventory) {

        var inventory = findByInventoryId(fromInventory.inventoryId());
        var user = findByUser(fromInventory.userId());

        validateQuantityReturn(fromInventory.quantityTaken());
        validateUserTakedFromInventory(user);

        int sumQuantity = sumQuantity(inventory.getQuantityCurrent(),
                                                                fromInventory.quantityTaken());

        saveHistoryAndInventoryOnQuantityRestored(fromInventory,
                                                                                            user,
                                                                                            sumQuantity,
                                                                                            inventory.getQuantityInitial(),
                                                                                            inventory);

        inventory.setQuantityCurrent(sumQuantity);
        repository.save(inventory);

        var history = mapper.buildHistoryDto(
                fromInventory.quantityTaken(),
                InventoryAction.RETURN,
                inventory.getProduct(),
                user,
                fromInventory.inventoryId()
        );

        saveHistory(history);
        return mapper.toReturnedInventoryResponse(inventory, fromInventory.quantityTaken());
    }

    private UserEntity findByUser(Long userId) {
        return userService.findUserByIdOrElseThrow(userId);
    }

    private void validateUserTakedFromInventory(UserEntity user) {
        if (!historyService.validateUserTakedFromInventory(user, InventoryAction.TAKE))
            throw new ResourceNotFoundException("You can't return it if you didn't pick it up");
    }

    private int sumQuantity(int quantityInitial, int quantityTaken) {
        return quantityInitial + quantityTaken;
    }

    private int subtractQuantity(int quantityInitial, int quantityTaken) {
        return quantityInitial - quantityTaken;
    }

    private void saveHistoryAndInventoryOnQuantityRestored(TakeFromInventory fromInventory, UserEntity user, int quantityToReturn, int totalToReturn, InventoryEntity inventory) {
        if (quantityToReturn == totalToReturn) {
            inventory.setQuantityCurrent(quantityToReturn);
            repository.save(inventory);

            var history = mapper.buildHistoryDto(
                    fromInventory.quantityTaken(),
                    InventoryAction.RETURN,
                    inventory.getProduct(),
                    user,
                    fromInventory.inventoryId()
            );

            saveHistory(history);
        } else {
            throw new QuantityRestoredException("quantity already restored");
        }
    }

    public void validateQuantityReturn(int quantityReturned) {
        if (quantityReturned <= 0)
            throw new InvalidQuantityException("Quantity must be greater than zero");
    }

    public void validateQuantityTaken(int quantityToReturn, int availableQuantity) {
        if (quantityToReturn <= 0)
            throw new InvalidQuantityException("Quantity must be greater than zero");

        if (availableQuantity == 0)
            throw new QuantitySoldOutException("quantity sold out");
    }

    private InventoryEntity findByInventoryId(String inventoryId) {
        return repository.findByInventoryId(inventoryId).orElseThrow(() -> new ResourceNotFoundException("InventoryId not found"));
    }

    private void saveHistory(HistoryEntityDTO dto) {
        historyService.save(dto);
    }

    public Page<InventoryEntityResponseDTO> filterByQuantity(Integer quantity, int pageNumber, int pageSize) {

        var specification = buildSpecification(quantity);

        Pageable pageable = PageRequest.of(pageNumber,
                                                                            pageSize,
                                                                            Sort.by(Sort.Order.asc("quantityInitial")));

        return repository
                .findAll(specification, pageable)
                .map(mapper::toResponseEntityInventory);
    }

    private Specification<InventoryEntity> buildSpecification(Integer quantity) {
        Specification<InventoryEntity> specification = null;

        if (quantity != null) {
            specification = equalsQuantity(quantity);
        }

        return specification;
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
