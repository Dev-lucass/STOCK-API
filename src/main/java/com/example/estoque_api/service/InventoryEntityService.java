package com.example.estoque_api.service;

import com.example.estoque_api.dto.internal.HistoryEntityDTO;
import com.example.estoque_api.dto.request.InventoryEntityDTO;
import com.example.estoque_api.dto.request.TakeFromInventory;
import com.example.estoque_api.dto.response.entity.InventoryEntityResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryEntityReturnResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryEntityTakeResponseDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.exceptions.DuplicateResouceException;
import com.example.estoque_api.exceptions.InvalidQuantityException;
import com.example.estoque_api.exceptions.QuantitySoldOutException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.mapper.HistoryEntityMapper;
import com.example.estoque_api.mapper.InventoryEntityMapper;
import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.model.ToolEntity;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.repository.InventoryEntityRepository;
import jakarta.transaction.Transactional;
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
    private final HistoryEntityMapper historyMapper;
    private final ToolEntityService toolService;
    private final UserEntityService userService;
    private final HistoryEntityService historyService;

    public InventoryEntityResponseDTO save(InventoryEntityDTO dto) {

        var tool = toolService.findToolByIdOrElseThrow(dto.idTool());

        validationInventoryToolIsDuplicatedOnCreate(tool);

        var inventoryEntityMapped = mapper
                .toEntityInventory(dto, tool);

        inventoryEntityMapped.setInventoryId(UUID.randomUUID().toString());

        var inventorySaved = repository.save(inventoryEntityMapped);
        return mapper.toResponseEntityInventory(inventorySaved);
    }

    public List<InventoryEntityResponseDTO> findAllByToolIsActive() {
        return repository.findAllByToolActiveTrue()
                .stream()
                .map(mapper::toResponseEntityInventory)
                .toList();
    }

    public InventoryEntityResponseDTO update(Long id, InventoryEntityDTO dto) {
        var inventory = findInventoryByIdOrElseThrow(id);

        var tool = toolService
                .findToolByIdOrElseThrow(dto.idTool());

        validateInventoryTooltIsDuplicatedOnUpdate(tool, id);

        int currentQuantity = calculateCurrentQuantity(inventory, dto);
        int initialQuantity = calculateInitialQuantity(inventory, dto);

        mapper.updateEntity(currentQuantity,
                initialQuantity,
                tool,
                inventory);

        var inventoryUpdated = repository.save(inventory);
        return mapper.toResponseEntityInventory(inventoryUpdated);
    }

    private int calculateCurrentQuantity(InventoryEntity inventory, InventoryEntityDTO dto) {
        return inventory.getQuantityCurrent() + dto.quantity();
    }

    private int calculateInitialQuantity(InventoryEntity inventory, InventoryEntityDTO dto) {
        return inventory.getQuantityInitial() + dto.quantity();
    }

    @Transactional
    public InventoryEntityTakeResponseDTO takeFromInventory(TakeFromInventory fromInventory) {

        var inventory = findByInventoryId(fromInventory.inventoryId());
        var user = findByUser(fromInventory.userId());

        validateUserIsActive(user);

        validateIsActiveTool(inventory.getTool());
        validateQuantityTaken(fromInventory.quantityTaken(), inventory.getQuantityCurrent());

        startUsageTool(inventory.getTool());

        int subtracted = subtractQuantity(inventory.getQuantityCurrent(),
                fromInventory.quantityTaken());

        inventory.setQuantityCurrent(subtracted);
        repository.save(inventory);

        var history = historyMapper.buildHistoryDto(
                fromInventory.quantityTaken(),
                InventoryAction.TAKE,
                inventory.getTool(),
                user,
                fromInventory.inventoryId()
        );

        saveHistory(history);
        return mapper.toTakeInventoryResponse(inventory,
                fromInventory.quantityTaken(),
                inventory.getTool().getUsageCount());
    }

    @Transactional
    public InventoryEntityReturnResponseDTO returnFromInventory(TakeFromInventory fromInventory) {

        var inventory = findByInventoryId(fromInventory.inventoryId());
        var user = findByUser(fromInventory.userId());

        validateUserIsActive(user);
        validateReturnProcess(user, inventory, fromInventory.quantityTaken());

        updateInventoryStock(inventory, fromInventory.quantityTaken());
        returnTool(inventory.getTool());

        createAndSaveHistory(fromInventory, inventory, user, InventoryAction.RETURN);

        return mapper.toReturnedInventoryResponse(
                inventory,
                fromInventory.quantityTaken(),
                inventory.getTool().getUsageCount(),
                inventory.getTool().getUsageTime()
        );
    }

    private void returnTool(ToolEntity tool){
        toolService.returnTool(tool);
    }

    private void validateReturnProcess(UserEntity user, InventoryEntity inventory, int quantity) {
        validateIsActiveTool(inventory.getTool());
        validateQuantityReturn(quantity);
        validateUserTakedFromInventory(user);
        historyService.validateTotalAmountThatTheUserMust(user, quantity);
    }

    private void validateUserIsActive(UserEntity user){
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

    private void createAndSaveHistory(TakeFromInventory dto, InventoryEntity inv, UserEntity user, InventoryAction action) {
        var historyDto = historyMapper.buildHistoryDto(
                dto.quantityTaken(),
                action,
                inv.getTool(),
                user,
                dto.inventoryId()
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

    private void validationInventoryToolIsDuplicatedOnCreate(ToolEntity Tool) {
        if (repository.existsByTool(Tool))
            throw new DuplicateResouceException("Tool already registered in inventory");
    }

    private void validateIsActiveTool(ToolEntity tool) {
        if (toolService.validateToolIsInactive(tool.getId()))
            throw new DuplicateResouceException("Tool not found in inventory.");
    }

    private void validateInventoryTooltIsDuplicatedOnUpdate(ToolEntity Tool, Long id) {
        if (repository.existsByToolAndIdNot(Tool, id))
            throw new DuplicateResouceException("Tool already registered in inventory");
    }

    private InventoryEntity findInventoryByIdOrElseThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid inventory id"));
    }
}
