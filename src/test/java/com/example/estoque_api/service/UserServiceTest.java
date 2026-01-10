package com.example.estoque_api.service;

import com.example.estoque_api.dto.request.filter.UserFilterDTO;
import com.example.estoque_api.dto.request.persist.UserDTO;
import com.example.estoque_api.dto.response.entity.UserResponseDTO;
import com.example.estoque_api.dto.response.filter.UserFilterResponseDTO;
import com.example.estoque_api.exceptions.DuplicateResouceException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.mapper.UserMapper;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.repository.UserRepository;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @Mock
    private HistoryService historyService;

    @Test
    void save_ValidDto_ReturnsResponse() {
        var dto = UserDTO.builder().cpf("123").build();
        var entity = new UserEntity();
        var response = UserResponseDTO.builder().id(1L).build();

        when(repository.existsByCpf(anyString()))
                .thenReturn(false);
        when(mapper.toEntityUser(dto))
                .thenReturn(entity);
        when(repository.save(entity))
                .thenReturn(entity);
        when(mapper.toResponseEntityUser(entity))
                .thenReturn(response);

        var result = service.save(dto);

        assertNotNull(result);
        assertEquals(1L, result.id());
    }

    @Test
    void save_DuplicateCpf_ThrowsException() {
        var dto = UserDTO.builder().cpf("123").build();

        when(repository.existsByCpf("123"))
                .thenReturn(true);

        assertThrows(DuplicateResouceException.class, () -> service.save(dto));
    }

    @Test
    void update_ExistingUser_ReturnsResponse() {
        var id = 1L;
        var dto = UserDTO.builder().cpf("123").build();
        var entity = new UserEntity();
        var response = UserResponseDTO.builder().id(id).build();

        when(repository.findById(id))
                .thenReturn(Optional.of(entity));
        when(repository.existsByCpfAndIdNot("123", id))
                .thenReturn(false);
        when(repository.save(entity))
                .thenReturn(entity);
        when(mapper.toResponseEntityUser(entity))
                .thenReturn(response);

        var result = service.update(id, dto);

        assertNotNull(result);
        verify(mapper).updateEntity(entity, dto);
    }

    @Test
    void validateUserIsActive_UserInactive_ThrowsException() {
        var user = UserEntity.builder().active(false).build();

        assertThrows(ResourceNotFoundException.class, () -> service.validateUserIsActive(user));
    }

    @Test
    void disableById_UserExistsAndNoDebt_DeactivatesUser() {
        var id = 1L;
        var user = UserEntity.builder().active(true).build();

        when(repository.findById(id))
                .thenReturn(Optional.of(user));
        doNothing().when(historyService).validateUserWhetherUserOwes(user);

        service.disableById(id);

        assertFalse(user.getActive());
    }

    @Test
    void findAll_ValidParams_ReturnsPage() {
        var filter = UserFilterDTO.builder().build();
        var pageable = mock(Pageable.class);
        var entity = new UserEntity();
        var page = new PageImpl<>(List.of(entity));
        var response = UserFilterResponseDTO.builder().build();

        when(repository.findAll(any(Predicate.class), any(Pageable.class)))
                .thenReturn(page);
        when(mapper.toFilterResponse(entity))
                .thenReturn(response);

        var result = service.findAll(filter, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void findUserByIdOrElseThrow_UserNotFound_ThrowsException() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findUserByIdOrElseThrow(1L));
    }
}