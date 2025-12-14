package com.example.estoque_api.validation;

import com.example.estoque_api.exceptions.DuplicateResouceException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.repository.UserEntityRepository;
import org.springframework.stereotype.Component;

@Component
public class UserEntityValidation {

    private final UserEntityRepository repository;

    public UserEntityValidation(UserEntityRepository repository) {
        this.repository = repository;
    }

    public void validationUserEntityIsDuplicatedOnCreate(UserEntity user) {
        repository.findByCpf(user.getCpf())
                .ifPresent(userFounded -> {
                    throw new DuplicateResouceException("CPF already registered");
                });
    }

    public void validationUserEntityIsDuplicatedOnUpdate(UserEntity user) {
        repository.findByCpf(user.getCpf())
                .ifPresent(userFounded -> {
                    if (!userFounded.getId().equals(user.getId())) throw new DuplicateResouceException("CPF already registered");
                });
    }

    public UserEntity validationUserEntityIdIsValid(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Invalid ID"));
    }

}
