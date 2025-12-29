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
        ToolEntity entity = mapper.toEntityTool(dto);
        return mapper.toResponseEntityTool(repository.save(entity));
    }

    public List<ToolEntityResponseDTO> findAllisActive(){
        return repository.findAllByActiveTrue()
                .stream()
                .map(mapper::toResponseEntityTool)
                .toList();
    }

    public List<ToolEntityResponseDTO> findAllisDisable(){
        return repository.findAllByActiveFalse()
                .stream()
                .map(mapper::toResponseEntityTool)
                .toList();
    }

    public ToolEntityResponseDTO update(Long id, ToolEntityDTO dto) {
        ToolEntity entity = findToolByIdOrElseThrow(id);
        validateDuplicateOnUpdate(dto.name(), id);
        mapper.updateEntity(entity, dto);
        return mapper.toResponseEntityTool(repository.save(entity));
    }

    @Transactional
    public void startUsage(ToolEntity tool) {
        validateToolAvailability(tool);

        applyDegradation(tool);
        tool.setUsageCount(tool.getUsageCount() + 1);
        tool.setUsageTime(LocalTime.now());

        repository.save(tool);
    }

    @Transactional
    public void disableById(Long id) {
        var entity = findToolByIdOrElseThrow(id);
        entity.setActive(false);
        entity.setCurrentLifeCycle(0.0);
        repository.save(entity);
    }

    @Transactional
    public void returnTool(ToolEntity tool) {
        tool.setUsageTime(null);
        repository.save(tool);
    }

    private void applyDegradation(ToolEntity tool) {
        double newLife = tool.getCurrentLifeCycle() - tool.getDegradationRate();
        tool.setCurrentLifeCycle(Math.max(0, newLife));

        if (tool.getCurrentLifeCycle() <= tool.getMinimumViableLife()) {
            tool.setActive(false);
        }
    }

    private void validateToolAvailability(ToolEntity tool) {
        if (Boolean.FALSE.equals(tool.getActive()))
            throw new DamagedToolException("Tool " + tool.getName() + " is inactive.");

        if (tool.getCurrentLifeCycle() <= tool.getMinimumViableLife())
            throw new DamagedToolException("Tool " + tool.getName() + " reached end of life cycle.");
    }

    public List<ToolEntityResponseDTO> findAllByStatus(boolean active) {
        return (active ? repository.findAllByActiveTrue() : repository.findAllByActiveFalse())
                .stream()
                .map(mapper::toResponseEntityTool)
                .toList();
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
        Specification<ToolEntity> spec = (name != null) ? likeName(name) : null;
        return repository.findAll(spec, pageable);
    }
}