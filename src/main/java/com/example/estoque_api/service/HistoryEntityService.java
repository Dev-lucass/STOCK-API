package com.example.estoque_api.service;

import com.example.estoque_api.dto.internal.HistoryEntityDTO;
import com.example.estoque_api.dto.response.entity.HistoryEntityResponseDTO;
import com.example.estoque_api.mapper.HistoryEntityMapper;
import com.example.estoque_api.model.HistoryEntity;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.repository.HistoryEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HistoryEntityService {

    private final HistoryEntityRepository repository;
    private final HistoryEntityMapper mapper;

    public void save(HistoryEntityDTO dto) {
        var historyEntityMapped = mapper.toEntityHistory(dto);
        repository.save(historyEntityMapped);
    }

    public List<HistoryEntityResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponseEntityHistory)
                .toList();
    }

    public Optional<HistoryEntity> findByUser(UserEntity user) {
        return repository.findByUser(user);
    }
}
