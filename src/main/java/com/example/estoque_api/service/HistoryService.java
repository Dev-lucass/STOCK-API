package com.example.estoque_api.service;

import com.example.estoque_api.dto.internal.HistoryDTO;
import com.example.estoque_api.dto.request.filter.HistoryFilterDTO;
import com.example.estoque_api.dto.response.entity.HistoryResponseDTO;
import com.example.estoque_api.dto.response.filter.HistoryFilterResponseDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.exceptions.InvalidQuantityException;
import com.example.estoque_api.exceptions.userMustStillReturnBeforeBeingDeactivated;
import com.example.estoque_api.mapper.HistoryMapper;
import com.example.estoque_api.model.HistoryEntity;
import com.example.estoque_api.model.ToolEntity;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.predicate.HistoryPredicate;
import com.example.estoque_api.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository repository;
    private final HistoryMapper mapper;

    void save(HistoryDTO dto) {
        var historyEntityMapped = mapper.toEntityHistory(dto);
        repository.save(historyEntityMapped);
    }

    public boolean validateUserTakedFromInventory(UserEntity user, InventoryAction action) {
        return repository.existsByUserAndAction(user, action);
    }

    public List<HistoryResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponseEntityHistory)
                .toList();
    }

    public void validateTotalAmountThatTheUserMustAndResetTimeUsage(UserEntity user, ToolEntity tool, int quantityReturned) {

        var totalTaken = calculateTotalTaken(user);
        var totalReturned = calculateTotalReturned(user);
        var currentDebt = totalTaken - totalReturned;

        if (quantityReturned > currentDebt) {
            throw new InvalidQuantityException("You cannot return " + quantityReturned + ", you only have " + currentDebt + " pending.");
        }
    }

    public int currentDebt(UserEntity user) {
        var totalTaken = calculateTotalTaken(user);
        var totalReturned = calculateTotalReturned(user);
        return subtractQuantity(totalTaken, totalReturned);
    }

    private int subtractQuantity(int totalTaken, int totalReturned) {
        return totalTaken - totalReturned;
    }

    public void validateUserWhetherUserOwes(UserEntity user) {

        var totalTaken = calculateTotalTaken(user);
        var totalReturned = calculateTotalReturned(user);
        var currentDebt = totalTaken - totalReturned;

        if (currentDebt > 0)
            throw new userMustStillReturnBeforeBeingDeactivated("user must still return to stock before being deactivated");
    }

    private int calculateTotalTaken(UserEntity user) {
        return repository.findByUser(user).stream()
                .filter(h -> h.getAction() == InventoryAction.TAKE)
                .mapToInt(HistoryEntity::getQuantityTaken)
                .sum();
    }

    private int calculateTotalReturned(UserEntity user) {
        return repository.findByUser(user).stream()
                .filter(h -> h.getAction() == InventoryAction.RETURN)
                .mapToInt(HistoryEntity::getQuantityTaken)
                .sum();
    }

    public Page<HistoryFilterResponseDTO> findAll(HistoryFilterDTO filter, Pageable pageable) {
        var predicate = HistoryPredicate.build(filter);
        var page = repository.findAll(predicate, pageable);
        return page.map(mapper::toFilterResponse);
    }
}
