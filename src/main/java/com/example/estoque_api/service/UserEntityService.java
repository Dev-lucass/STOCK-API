package com.example.estoque_api.service;

import com.example.estoque_api.exceptions.DuplicateResouceException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
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

    public void validationUserEntityIsDuplicatedOnCreate(UserEntity user) {
       if (repository.existsByCpf(user.getCpf()))
           throw  new DuplicateResouceException("User already registered");
    }

    public void validationUserEntityIsDuplicatedOnUpdate(UserEntity user) {
        if (repository.existsByCpfAndNot(
                user.getCpf(),
                user.getId())
        ) throw  new DuplicateResouceException("User already registered");
    }

    public UserEntity validationUserEntityIdIsValid(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid user ID"));
    }
}
