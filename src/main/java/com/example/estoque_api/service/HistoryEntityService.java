package com.example.estoque_api.service;

import com.example.estoque_api.dto.internal.HistoryEntityDTO;
import com.example.estoque_api.model.HistoryEntity;
import com.example.estoque_api.repository.HistoryEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryEntityService {

    private final HistoryEntityRepository repository;

    public void save(HistoryEntityDTO dto) {
    }

    public List<HistoryEntity> findAll() {
        return null;
    }
}
