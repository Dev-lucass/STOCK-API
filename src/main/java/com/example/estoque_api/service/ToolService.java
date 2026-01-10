package com.example.estoque_api.service;

import com.example.estoque_api.dto.request.filter.ToolFilterDTO;
import com.example.estoque_api.dto.request.persist.ToolDTO;
import com.example.estoque_api.dto.response.entity.ToolResponseDTO;
import com.example.estoque_api.dto.response.filter.ToolFilterResponseDTO;
import com.example.estoque_api.exceptions.DamagedToolException;
import com.example.estoque_api.exceptions.DuplicateResouceException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.exceptions.ToolInUseException;
import com.example.estoque_api.mapper.ToolMapper;
import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.model.ToolEntity;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.predicate.ToolPredicate;
import com.example.estoque_api.repository.ToolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class ToolService {

    private final ToolRepository repository;
    private final ToolMapper mapper;
    private final HistoryService historyService;

    public ToolResponseDTO save(ToolDTO dto) {
        validateDuplicateOnCreate(dto.name());
        ToolEntity entity = mapper.toEntityTool(dto);
        return mapper.toResponseEntityTool(repository.save(entity));
    }

    public ToolResponseDTO update(Long id, ToolDTO dto) {
        ToolEntity entity = findToolByIdOrElseThrow(id);
        validateDuplicateOnUpdate(dto.name(), id);
        mapper.updateEntity(entity, dto);
        return mapper.toResponseEntityTool(repository.save(entity));
    }

    @Transactional(noRollbackFor = DamagedToolException.class)
    public void startUsage(ToolEntity tool) {
        throwIfToolIsDamaged(tool);
        applyDegradation(tool);
        tool.setUsageCount(tool.getUsageCount() + 1);
        tool.setLastUsageStart(LocalTime.now().truncatedTo(ChronoUnit.SECONDS));
        repository.save(tool);
    }

    @Transactional
    public void disableById(Long id) {
        var tool = findToolByIdOrElseThrow(id);
        validateWhetherCanDeactivateTool(tool);
        tool.setActive(false);
    }

    private void validateWhetherCanDeactivateTool(ToolEntity tool) {
        if (tool.getInUse()) throw new ToolInUseException("You cannot deactivate a tool because it is being used");
    }

    @Transactional
    public void returnTool(ToolEntity tool, UserEntity user) {
        int currentDebt = currentDebt(user);
        resetTimeUsageToolIfCurrentDebtIsZero(tool, currentDebt);
    }

    public void takeTool(InventoryEntity inventory, int subtractQuantity) {
        inventory.setQuantityCurrent(subtractQuantity);
        inventory.getTool().setInUse(true);
    }

    private void resetTimeUsageToolIfCurrentDebtIsZero(ToolEntity tool, int currentDebt) {
        if (currentDebt == 0) {
            LocalTime tempoCalculado = calculateUsageTime(tool);
            tool.setUsageTime(tempoCalculado);
            tool.setLastUsageStart(null);
            tool.setInUse(false);
            repository.save(tool);
        }
    }

    private int currentDebt(UserEntity user) {
        return historyService.currentDebt(user);
    }

    public static LocalTime calculateUsageTime(ToolEntity tool) {

        var duration = Duration.between(
                tool.getLastUsageStart().truncatedTo(ChronoUnit.SECONDS),
                LocalTime.now().truncatedTo(ChronoUnit.SECONDS)
        );

        if (duration.isNegative())
            duration = duration.plusDays(1);

        return LocalTime.MIDNIGHT.plus(duration);
    }

    private void applyDegradation(ToolEntity tool) {
        var lifeDegrated = calculateLifeDegrated(tool);
        tool.setCurrentLifeCycle(lifeDegrated);
        disableToolIfIsDamaged(tool);
    }

    private void disableToolIfIsDamaged(ToolEntity tool) {
        if (validateIfToolIsDamaged(tool)) {
            tool.setActive(false);
            damagedToolException(tool);
        }
    }

    private void damagedToolException(ToolEntity tool) {
        throw new DamagedToolException("Tool " + tool.getName() + " reached end of life cycle.");
    }

    private boolean validateIfToolIsDamaged(ToolEntity tool) {
        return tool.getCurrentLifeCycle() < tool.getMinimumViableLife();
    }

    private double calculateLifeDegrated(ToolEntity tool) {
        return tool.getCurrentLifeCycle() - tool.getDegradationRate();
    }

    public void throwIfToolIsDamaged(ToolEntity tool) {
        if (validateIfToolIsDamaged(tool)) damagedToolException(tool);
    }

    public ToolEntity findToolByIdOrElseThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tool not found with id: " + id));
    }

    public boolean validateToolIsInactive(Long idTool) {
        return repository.existsByIdAndActiveFalse(idTool);
    }

    private void validateDuplicateOnCreate(String name) {
        if (repository.existsByName(name))
            throw new DuplicateResouceException("Tool already registered: " + name);
    }

    private void validateDuplicateOnUpdate(String name, Long id) {
        if (repository.existsByNameAndIdNot(name, id))
            throw new DuplicateResouceException("Tool toolName already in use");
    }

    public Page<ToolFilterResponseDTO> findAll(ToolFilterDTO filter, Pageable pageable) {
        var build = ToolPredicate.build(filter);
        var page = repository.findAll(build, pageable);
        return page.map(mapper::toFilterResponse);
    }
}