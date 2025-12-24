package com.example.estoque_api.service;

import com.example.estoque_api.dto.request.ToolEntityDTO;
import com.example.estoque_api.dto.response.entity.ToolEntityResponseDTO;
import com.example.estoque_api.exceptions.DamagedToolException;
import com.example.estoque_api.exceptions.DuplicateResouceException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.mapper.ToolEntityMapper;
import com.example.estoque_api.model.ToolEntity;
import com.example.estoque_api.repository.ToolEntityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import static com.example.estoque_api.repository.specs.ToolEntitySpec.likeName;

@Service
@RequiredArgsConstructor
public class ToolEntityService {

    private final ToolEntityRepository repository;
    private final ToolEntityMapper mapper;

    public ToolEntityResponseDTO save(ToolEntityDTO dto) {
        validateDuplicateOnCreate(dto.name());
        var userEntityMapped = mapper.toEntityTool(dto);
        var toolSaved = repository.save(userEntityMapped);
        return mapper.toResponseEntityTool(toolSaved);
    }

    public List<ToolEntityResponseDTO> findAllIsActive() {
        return repository.findAllByActiveTrue()
                .stream()
                .map(mapper::toResponseEntityTool)
                .toList();
    }

    public List<ToolEntityResponseDTO> findAllIsNotActive() {
        return repository.findAllByActiveFalse()
                .stream()
                .map(mapper::toResponseEntityTool)
                .toList();
    }

    public ToolEntityResponseDTO update(Long id, ToolEntityDTO dto) {
        var entity = findToolByIdOrElseThrow(id);
        validateDuplicateOnUpdate(dto.name(), id);
        mapper.updateEntity(entity, dto);
        var toolUpdated = repository.save(entity);
        return mapper.toResponseEntityTool(toolUpdated);
    }

    @Transactional
    public void disableById(Long id) {
        var entity = findToolByIdOrElseThrow(id);
        entity.setActive(false);
    }

    public void startUsage(ToolEntity tool) {
        validateLifeCycleTool(tool);
        readjustUsefulLife(tool);
        startTimeUsageTool(tool);
        setUsageTool(tool);
    }

    public void returnTool(ToolEntity tool) {
        validateLifeCycleTool(tool);
        timeUsagedAfterReturn(tool);
        repository.save(tool);
    }

    private void timeUsagedAfterReturn(ToolEntity tool) {
        LocalTime startTime = tool.getUsageTime();
        LocalTime endTime = LocalTime.now();

        Duration duration = Duration.between(startTime, endTime);

        long seconds = duration.getSeconds();

        LocalTime usageDurationAsTime = LocalTime.ofSecondOfDay(seconds);

        tool.setUsageTime(usageDurationAsTime);
    }

    private void startTimeUsageTool(ToolEntity tool) {
        if (tool.getUsageTime() == null) tool.setUsageTime(LocalTime.now());
    }

    private void validateLifeCycleTool(ToolEntity tool) {
        if (tool.getCurrentLifeCycle() <= (100.0 - 60.0)) {
            disableToolByLifeCycle(tool);
            throw new DamagedToolException("End of useful life reached");
        }
    }

    private void setUsageTool(ToolEntity tool) {
        tool.setUsageCount(tool.getUsageCount() + 1);
    }

    private void readjustUsefulLife(ToolEntity tool) {
        double currentLife = tool.getCurrentLifeCycle();
        double updatedLife = currentLife - 1.5;
        tool.setCurrentLifeCycle(updatedLife);
    }

    private void disableToolByLifeCycle(ToolEntity tool) {
        if (tool.getCurrentLifeCycle() <= 40) {
            tool.setActive(false);
            repository.save(tool);
        }
    }

    public Page<ToolEntity> filterByNamePageable(String name, int pageNumber, int pageSize) {

        var specification = buildSpecification(name);

        Pageable pageable = PageRequest.of(
                pageNumber,
                pageSize,
                Sort.by("name").ascending());

        return repository.findAll(specification, pageable);
    }

    private Specification<ToolEntity> buildSpecification(String name) {
        Specification<ToolEntity> specification = null;

        if (name != null) {
            specification = likeName(name);
        }

        return specification;
    }

    private void validateDuplicateOnCreate(String name) {
        if (repository.existsByName(name))
            throw new DuplicateResouceException("Tool already registered");
    }

    private void validateDuplicateOnUpdate(String name, Long id) {
        if (repository.existsByNameAndIdNot(name, id))
            throw new DuplicateResouceException("Tool already registered");
    }

    public ToolEntity findToolByIdOrElseThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid tool id"));
    }
}
