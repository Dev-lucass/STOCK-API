package com.example.estoque_api.service;

import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.repository.UserEntityRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserEntityService {

    private final UserEntityRepository repository;

    public UserEntityService(UserEntityRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public UserEntity save(UserEntity user) {
        return repository.save(user);
    }

    public List<UserEntity> findAll() {
        return repository.findAll();
    }

    @Transactional
    public UserEntity update(Long id, UserEntity userUpdated) {

        UserEntity userEntityFounded = repository.findById(id).orElseThrow(() -> new RuntimeException("Id user not found ..."));

        userEntityFounded.setUsername(userUpdated.getUsername());
        userEntityFounded.setCpf(userUpdated.getCpf());
        userEntityFounded.setAdress(userUpdated.getAdress());

        return userEntityFounded;
    }

    @Transactional
    public void deleteById(Long id) {
        UserEntity userEntityFounded = repository.findById(id).orElseThrow(() -> new RuntimeException("Id user not found ..."));
        repository.delete(userEntityFounded);
    }


}
