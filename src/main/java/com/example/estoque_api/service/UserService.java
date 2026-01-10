package com.example.estoque_api.service;

import com.example.estoque_api.dto.request.filter.UserFilterDTO;
import com.example.estoque_api.dto.request.persist.UserDTO;
import com.example.estoque_api.dto.response.entity.UserResponseDTO;
import com.example.estoque_api.dto.response.filter.UserFilterResponseDTO;
import com.example.estoque_api.exceptions.DuplicateResouceException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.mapper.UserMapper;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.predicate.UserPredicate;
import com.example.estoque_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final HistoryService historyService;

    public UserResponseDTO save(UserDTO dto) {
        validateDuplicateOnCreate(dto.cpf());
        var userEntityMapped = mapper.toEntityUser(dto);
        var userSaved = repository.save(userEntityMapped);
        return mapper.toResponseEntityUser(userSaved);
    }

    public UserResponseDTO update(Long id, UserDTO dto) {
        var entity = findUserByIdOrElseThrow(id);
        validateDuplicateOnUpdate(dto.cpf(), id);
        mapper.updateEntity(entity, dto);
        var userUpdated = repository.save(entity);
        return mapper.toResponseEntityUser(userUpdated);
    }

    public void validateUserIsActive(UserEntity user) {
        if (Boolean.FALSE.equals(user.getActive())) {
            throw new ResourceNotFoundException("User is inactive and cannot perform this action.");
        }
    }

    @Transactional
    public void disableById(Long id) {
        var user = findUserByIdOrElseThrow(id);
        validateUserWhetherUserOwes(user);
        user.setActive(false);
    }

    public void validateUserWhetherUserOwes(UserEntity user) {
        historyService.validateUserWhetherUserOwes(user);
    }

    public Page<UserFilterResponseDTO> findAll(UserFilterDTO filter, Pageable pageable) {
        var predicate = UserPredicate.build(filter);
        var page = repository.findAll(predicate, pageable);
        return page.map(mapper::toFilterResponse);
    }

    private void validateDuplicateOnCreate(String cpf) {
        if (repository.existsByCpf(cpf))
            throw new DuplicateResouceException("User already registered");
    }

    private void validateDuplicateOnUpdate(String cpf, Long id) {
        if (repository.existsByCpfAndIdNot(cpf, id))
            throw new DuplicateResouceException("User already registered");
    }

    public UserEntity findUserByIdOrElseThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid user id"));
    }
}
