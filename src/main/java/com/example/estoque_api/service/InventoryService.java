package com.example.estoque_api.service;

import com.example.estoque_api.dto.internal.HistoryDTO;
import com.example.estoque_api.dto.request.filter.InventoryFilterDTO;
import com.example.estoque_api.dto.request.persist.InventoryDTO;
import com.example.estoque_api.dto.request.persist.TakeFromInventory;
import com.example.estoque_api.dto.response.entity.InventoryResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryReturnResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryTakeResponseDTO;
import com.example.estoque_api.dto.response.filter.InventoryFilterResponseDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.exceptions.DuplicateResouceException;
import com.example.estoque_api.exceptions.InvalidQuantityException;
import com.example.estoque_api.exceptions.QuantitySoldOutException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.mapper.HistoryMapper;
import com.example.estoque_api.mapper.InventoryMapper;
import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.model.ToolEntity;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.predicate.InventoryPredicate;
import com.example.estoque_api.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository repository;
    private final InventoryMapper mapper;
    private final HistoryMapper historyMapper;
    private final ToolService toolService;
    private final UserService userService;
    private final HistoryService historyService;

    public InventoryResponseDTO save(InventoryDTO dto) {

        var tool = findToolById(dto);

        validationInventoryToolIsDuplicatedOnCreate(tool);

        var inventoryEntityMapped = mapper
                .toEntityInventory(dto, tool);

        var inventorySaved = repository.save(inventoryEntityMapped);
        return mapper.toResponseEntityInventory(inventorySaved);
    }

    public ToolEntity findToolById(InventoryDTO dto) {
        return toolService.findToolByIdOrElseThrow(dto.toolId());
    }


    public InventoryResponseDTO update(Long id, InventoryDTO dto) {

        var inventory = findInventoryByIdOrElseThrow(id);
        var tool = findToolByIdOrElseThrow(dto.toolId());

        validateInventoryToolIsDuplicatedOnUpdate(tool.getId(), id);

        var currentQuantity = calculateCurrentQuantity(inventory, dto);
        var initialQuantity = calculateInitialQuantity(inventory, dto);

        mapper.updateEntity(currentQuantity,
                initialQuantity,
                tool,
                inventory);

        var inventoryUpdated = repository.save(inventory);
        return mapper.toResponseEntityInventory(inventoryUpdated);
    }

    private ToolEntity findToolByIdOrElseThrow(Long toolId) {
        return toolService.findToolByIdOrElseThrow(toolId);
    }

    private int calculateCurrentQuantity(InventoryEntity inventory, InventoryDTO dto) {
        return inventory.getQuantityCurrent() + dto.quantity();
    }

    private int calculateInitialQuantity(InventoryEntity inventory, InventoryDTO dto) {
        return inventory.getQuantityInitial() + dto.quantity();
    }

    public InventoryTakeResponseDTO takeFromInventory(TakeFromInventory fromInventory) {

        var inventory = findInventoryByIdOrElseThrow(fromInventory.inventoryId());
        var user = findByUser(fromInventory.userId());

        validateUserIsActive(user);
        validateIfTheToolIsInactiveOrDamaged(inventory.getTool());
        validateQuantityTaken(fromInventory.quantityTaken(), inventory.getQuantityCurrent());
        startUsageTool(inventory.getTool());

        var subtracted = subtractQuantity(inventory.getQuantityCurrent(),
                fromInventory.quantityTaken());

        takeTool(inventory, subtracted);
        repository.save(inventory);

        var history = historyMapper.buildHistoryDto(
                fromInventory.quantityTaken(),
                InventoryAction.TAKE,
                inventory.getTool(),
                user,
                fromInventory.inventoryId(),
                inventory.getTool().getCurrentLifeCycle()
        );

        saveHistory(history);
        return mapper.toTakeInventoryResponse(inventory,
                fromInventory.quantityTaken(),
                inventory.getTool().getUsageCount());
    }

    private void takeTool(InventoryEntity inventory, int subtracted) {
        toolService.takeTool(inventory, subtracted);
    }

    public InventoryReturnResponseDTO returnFromInventory(TakeFromInventory fromInventory) {

        var inventory = findInventoryByIdOrElseThrow(fromInventory.inventoryId());
        var tool = inventory.getTool();
        var user = findByUser(fromInventory.userId());

        validateUserIsActive(user);
        validateReturnProcess(user, inventory, fromInventory.quantityTaken());

        var durationUsed = calculateUsageTimeTool(tool);

        updateInventoryStock(inventory, fromInventory.quantityTaken());
        createAndSaveHistory(fromInventory, inventory, user);
        returnTool(tool, user);

        return mapper.toReturnedInventoryResponse(
                inventory,
                fromInventory.quantityTaken(),
                tool.getUsageCount(),
                durationUsed
        );
    }

    private LocalTime calculateUsageTimeTool(ToolEntity tool) {
        return ToolService.calculateUsageTime(tool);
    }

    private void returnTool(ToolEntity tool, UserEntity user) {
        toolService.returnTool(tool, user);
    }

    private void validateReturnProcess(UserEntity user, InventoryEntity inventory, int quantity) {
        validateUserTakedFromInventory(user);
        validateQuantityReturn(quantity);
        validateIfTheToolIsInactiveOrDamaged(inventory.getTool());
        validateTotalAmountThatTheUserMustAndResetTimeUsage(user, inventory, quantity);
    }

    private void validateTotalAmountThatTheUserMustAndResetTimeUsage(UserEntity user, InventoryEntity inventory, int quantity) {
        historyService.validateTotalAmountThatTheUserMustAndResetTimeUsage(user, inventory.getTool(), quantity);
    }

    private void validateUserIsActive(UserEntity user) {
        userService.validateUserIsActive(user);
    }

    private void updateInventoryStock(InventoryEntity inventory, int quantityTaken) {
        int newQuantity = sumQuantity(inventory.getQuantityCurrent(), quantityTaken);

        if (newQuantity > inventory.getQuantityInitial()) {
            newQuantity = inventory.getQuantityInitial();
        }

        inventory.setQuantityCurrent(newQuantity);
        repository.save(inventory);
    }

    private void createAndSaveHistory(TakeFromInventory dto, InventoryEntity inventory, UserEntity user) {
        var historyDto = historyMapper.buildHistoryDto(
                dto.quantityTaken(),
                InventoryAction.RETURN,
                inventory.getTool(),
                user,
                dto.inventoryId(),
                inventory.getTool().getCurrentLifeCycle()
        );
        saveHistory(historyDto);
    }

    private void startUsageTool(ToolEntity tool) {
        toolService.startUsage(tool);
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

    public void validateQuantityReturn(int quantityReturned) {
        if (quantityReturned <= 0)
            throw new InvalidQuantityException("Quantity must be greater than zero");
    }

    public void validateQuantityTaken(int quantityToReturn, int availableQuantity) {
        if (quantityToReturn <= 0)
            throw new InvalidQuantityException("Quantity must be greater than zero");

        if (availableQuantity == 0)
            throw new QuantitySoldOutException("quantity sold out");

        if (quantityToReturn > availableQuantity)
            throw new InvalidQuantityException("The Quantity requested must be less than available quantity");
    }

    private void saveHistory(HistoryDTO dto) {
        historyService.save(dto);
    }

    public Page<InventoryFilterResponseDTO> findAll(InventoryFilterDTO filter, Pageable pageable) {
        var build = InventoryPredicate.build(filter);
        var page = repository.findAll(build, pageable);
        return page.map(mapper::toFilterResponse);
    }

    private void validationInventoryToolIsDuplicatedOnCreate(ToolEntity Tool) {
        if (repository.existsByTool(Tool))
            throw new DuplicateResouceException("Tool already registered in inventory");
    }

    private void validateIfTheToolIsInactiveOrDamaged(ToolEntity tool) {
        if (toolService.validateToolIsInactive(tool.getId()) && tool.getCurrentLifeCycle() > 40)
            throw new DuplicateResouceException("Tool is inactive.");

        toolService.throwIfToolIsDamaged(tool);
    }

    private void validateInventoryToolIsDuplicatedOnUpdate(Long toolId, Long inventoryId) {
        if (repository.existsByToolIdAndIdNot(toolId, inventoryId)) {
            throw new DuplicateResouceException("This tool " + toolId + " already has a record in another inventory.");
        }
    }

    private InventoryEntity findInventoryByIdOrElseThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid inventory id"));
    }
}