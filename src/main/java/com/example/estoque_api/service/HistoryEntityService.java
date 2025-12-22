package com.example.estoque_api.service;

import com.example.estoque_api.dto.internal.HistoryEntityDTO;
import com.example.estoque_api.dto.response.entity.HistoryEntityResponseDTO;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.mapper.HistoryEntityMapper;
import com.example.estoque_api.model.HistoryEntity;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.repository.HistoryEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryEntityService {

    private final HistoryEntityRepository repository;
    private final HistoryEntityMapper mapper;

    public HistoryEntityResponseDTO save(HistoryEntityDTO dto) {
        var historyEntityMapped = mapper.toEntityHistory(dto);
        var historySaved = repository.save(historyEntityMapped);
        return mapper.toResponseEntityHistory(historySaved);
    }

    public List<HistoryEntityResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponseEntityHistory)
                .toList();
    }

    public HistoryEntity findByUser (UserEntity user) {
        return repository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("User not found in history"));
    }
}
