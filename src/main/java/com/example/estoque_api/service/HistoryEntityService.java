package com.example.estoque_api.service;

import com.example.estoque_api.dto.internal.HistoryEntityDTO;
import com.example.estoque_api.dto.response.entity.HistoryEntityResponseDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.exceptions.InvalidQuantityException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.mapper.HistoryEntityMapper;
import com.example.estoque_api.model.HistoryEntity;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.repository.HistoryEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;
import static com.example.estoque_api.repository.specs.HistoryEntitySpec.*;

@Service
@RequiredArgsConstructor
public class HistoryEntityService {

    private final HistoryEntityRepository repository;
    private final HistoryEntityMapper mapper;

    void save(HistoryEntityDTO dto) {
        var historyEntityMapped = mapper.toEntityHistory(dto);
        repository.save(historyEntityMapped);
    }

    public boolean validateUserTakedFromInventory(UserEntity user, InventoryAction action) {
        return repository.existsByUserAndAction(user, action);
    }

    public List<HistoryEntityResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponseEntityHistory)
                .toList();
    }

    public void validateTotalAmountThatTheUserMust(UserEntity user, int quantityReturned) {
        int toReturn = calculateTotalToReturn(user);

        if (quantityReturned > toReturn)
            throw new InvalidQuantityException("You cannot return more than you received");

        if (toReturn - quantityReturned == quantityReturned)
            throw new InvalidQuantityException("Have you already returned the entire quantity you took");
    }

    private int calculateTotalToReturn(UserEntity user) {
        return repository.findByUser(user)
                .stream()
                .mapToInt(HistoryEntity::getQuantityTaken)
                .sum();
    }

    public Page<HistoryEntityResponseDTO> filterHistory(
            String nameTool,
            InventoryAction action,
            Integer quantity,
            Integer minQuantity,
            Integer maxQuantity,
            int pageNumber,
            int pageSize) {

        validateMinAndMaxQuantity(minQuantity, maxQuantity);
        validateAction(action);

        Specification<HistoryEntity> specification = buildSpecification(nameTool, action, quantity, minQuantity, maxQuantity);

        Pageable pageable = PageRequest.of(
                pageNumber,
                pageSize
        );

        return repository
                .findAll(specification, pageable)
                .map(mapper::toResponseEntityHistory);
    }

    public Specification<HistoryEntity> buildSpecification(
            String nameTool,
            InventoryAction action,
            Integer quantity,
            Integer minQuantity,
            Integer maxQuantity) {

        Specification<HistoryEntity> spec = (r, q, cb) -> cb.conjunction();

        if (action != null) {
            spec = spec.and(hasAction(action));
        }

        if (nameTool != null) {
            spec = spec.and(likeToolName(nameTool));
        }

        if (quantity != null) {
            spec = spec.and(equalsQuantity(quantity));
        }

        if (minQuantity != null) {
            spec = spec.and(minQuantity(minQuantity));
        }

        if (maxQuantity != null) {
            spec = spec.and(maxQuantity(maxQuantity));
        }

        return spec;
    }


    public void validateMinAndMaxQuantity(Integer min, Integer max) {
        if (min != null && max != null && min > max)
            throw new InvalidQuantityException("minQuantity cannot be greater than maxQuantity.");
    }

    public void validateAction(InventoryAction action) {
        if (action == null) return;
        switch (action) {
            case TAKE, RETURN -> {
            }
            default -> throw new ResourceNotFoundException("Invalid Action");
        }
    }
}
