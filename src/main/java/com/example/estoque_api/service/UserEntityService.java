package com.example.estoque_api.service;

import com.example.estoque_api.dto.request.UserEntityDTO;
import com.example.estoque_api.dto.response.entity.UserEntityResponseDTO;
import com.example.estoque_api.exceptions.DuplicateResouceException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.mapper.UserEntityMapper;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.repository.UserEntityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.estoque_api.repository.specs.UserEntitySpec.likeUsername;

@Service
@RequiredArgsConstructor
public class UserEntityService {

    private final UserEntityRepository repository;
    private final UserEntityMapper mapper;

    public UserEntityResponseDTO save(UserEntityDTO dto) {
        validateDuplicateOnCreate(dto.cpf());
        var userEntityMapped = mapper.toEntityUser(dto);
        var userSaved = repository.save(userEntityMapped);
        return mapper.toResponseEntityUser(userSaved);
    }

    public List<UserEntityResponseDTO> findAll() {
        return repository.findAllByActiveTrue()
                .stream()
                .map(mapper::toResponseEntityUser)
                .toList();
    }

    public UserEntityResponseDTO update(Long id, UserEntityDTO dto) {
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
        var entity = findUserByIdOrElseThrow(id);
        entity.setActive(false);
    }

    public Page<UserEntity> filterByUsernamePageable(String username, int pageNumber, int pageSize) {

        var specification = buildSpecification(username);

        Pageable pageable = PageRequest.of(
                pageNumber,
                pageSize,
                Sort.by("username").ascending());

        return repository.findAll(specification, pageable);
    }

    private Specification<UserEntity> buildSpecification(String username) {
        Specification<UserEntity> specification = null;

        if (username != null) {
            specification = likeUsername(username);
        }

        return specification;
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
