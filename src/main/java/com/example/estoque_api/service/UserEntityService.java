package com.example.estoque_api.service;

import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserEntityService {

    private final UserEntityRepository repository;

    public UserEntity save(UserEntity user) {
        return null;
    }

    public List<UserEntity> findAll() {
        return null;
    }

    public UserEntity update(Long id, UserEntity userUpdated) {
        return null;
    }

    public void deleteById(Long id) {

    }
}
