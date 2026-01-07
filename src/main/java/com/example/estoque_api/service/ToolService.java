package com.example.estoque_api.service;

import com.example.estoque_api.dto.request.ToolDTO;
import com.example.estoque_api.dto.response.entity.ToolResponseDTO;
import com.example.estoque_api.exceptions.DamagedToolException;
import com.example.estoque_api.exceptions.DuplicateResouceException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.mapper.ToolMapper;
import com.example.estoque_api.model.ToolEntity;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.repository.ToolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import static com.example.estoque_api.repository.specs.ToolSpec.likeName;

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

    public List<ToolResponseDTO> findAllisActive() {
        return repository.findAllByActiveTrue()
                .stream()
                .map(mapper::toResponseEntityTool)
                .toList();
    }

    public List<ToolResponseDTO> findAllisDisable() {
        return repository.findAllByActiveFalse()
                .stream()
                .map(mapper::toResponseEntityTool)
                .toList();
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
        tool.setUsageTime(LocalTime.now());
    }

    @Transactional
    public void disableById(Long id) {
        var entity = findToolByIdOrElseThrow(id);
        entity.setActive(false);
        entity.setCurrentLifeCycle(0.0);
    }

    @Transactional
    public void returnTool(ToolEntity tool, UserEntity user) {
        int currentDebt = currentDebt(user);
        resetTimeUsageToolIfCurrentDebtIsZero(tool, currentDebt);
    }

    private void resetTimeUsageToolIfCurrentDebtIsZero(ToolEntity tool, int currentDebt) {
        if (currentDebt == 0) tool.setUsageTime(LocalTime.MIDNIGHT);
    }

    private int currentDebt(UserEntity user) {
        return historyService.currentDebt(user);
    }

    public LocalTime calculateUsageTime(ToolEntity tool) {

        var usageTime = Duration
                .between(tool.getUsageTime(), LocalTime.now());

        if (usageTime.isNegative())
            usageTime = usageTime.plusDays(1);

        return LocalTime.MIDNIGHT.plus(usageTime);
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
            throw new DuplicateResouceException("Tool name already in use");
    }

    public Page<ToolEntity> filterByNamePageable(String name, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("name").ascending());
        var spec = buildSpecification(name);
        return repository.findAll(spec, pageable);
    }

    private Specification<ToolEntity> buildSpecification(String name) {
        Specification<ToolEntity> specification = null;
        if (name != null) specification = likeName(name);
        return specification;
    }
}