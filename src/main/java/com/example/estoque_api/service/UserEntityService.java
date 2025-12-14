package com.example.estoque_api.service;

import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.repository.UserEntityRepository;
import com.example.estoque_api.validation.UserEntityValidation;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserEntityService {

    private final UserEntityRepository repository;
    private final UserEntityValidation validation;

    public UserEntityService(UserEntityRepository repository, UserEntityValidation validation) {
        this.repository = repository;
        this.validation = validation;
    }

    @Transactional
    public UserEntity save(UserEntity user) {
        validation.validationUserEntityIsDuplicatedOnCreate(user);
        return repository.save(user);
    }

    public List<UserEntity> findAll() {
        return repository.findAll();
    }

    @Transactional
    public UserEntity update(Long id, UserEntity userUpdated) {

        UserEntity userEntityFounded = validation.validationUserEntityIdIsValid(id);
        validation.validationUserEntityIsDuplicatedOnUpdate(userUpdated);

        userEntityFounded.setUsername(userUpdated.getUsername());
        userEntityFounded.setCpf(userUpdated.getCpf());
        userEntityFounded.setAddress(userUpdated.getAddress());

        return userEntityFounded;
    }

    @Transactional
    public void deleteById(Long id) {
        UserEntity userEntityFounded = validation.validationUserEntityIdIsValid(id);
        repository.delete(userEntityFounded);
    }


}
