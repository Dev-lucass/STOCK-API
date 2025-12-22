package com.example.estoque_api.service;

import com.example.estoque_api.dto.request.UserEntityDTO;
import com.example.estoque_api.dto.response.entity.UserEntityResponseDTO;
import com.example.estoque_api.exceptions.DuplicateResouceException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.mapper.UserEntityMapper;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.repository.UserEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class UserEntityServiceTest {

    @Mock
    private UserEntityRepository repository;

    @Mock
    private UserEntityMapper mapper;

    @InjectMocks
    private UserEntityService service;

    private UserEntity entity;
    private UserEntityDTO dto;
    private UserEntityResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        entity = new UserEntity();
        entity.setId(1L);
        entity.setUsername("Lucas");
        entity.setCpf("123");
        entity.setAddress("Rua A");
        entity.setActive(true);

        dto = new UserEntityDTO(
                "Lucas",
                "123",
                "Rua A");

        responseDTO = new UserEntityResponseDTO(
                1L,
                "Lucas",
                LocalDate.now());
    }

    @Test
    void should_save_user_successfully() {
        when(repository.existsByCpf(dto.cpf()))
                .thenReturn(false);

        when(mapper.toEntityUser(dto))
                .thenReturn(entity);

        when(repository.save(entity))
                .thenReturn(entity);

        when(mapper.toResponseEntityUser(entity))
                .thenReturn(responseDTO);

        var result = service.save(dto);

        assertNotNull(result);
        assertEquals(responseDTO, result);
        verify(repository).existsByCpf(dto.cpf());
        verify(repository).save(entity);
    }

    @Test
    void should_throw_exception_when_saving_duplicated_cpf() {
        when(repository.existsByCpf(dto.cpf()))
                .thenReturn(true);

        assertThrows(DuplicateResouceException.class, () -> service.save(dto));

        verify(repository).existsByCpf(dto.cpf());
        verify(repository, never()).save(any());
    }

    @Test
    void should_return_all_active_users() {
        when(repository.findAllByActiveTrue()).thenReturn(List.of(entity));
        when(mapper.toResponseEntityUser(entity)).thenReturn(responseDTO);
        var result = service.findAll();
        assertEquals(1, result.size());
        verify(repository).findAllByActiveTrue();
    }

    @Test
    void should_update_user_successfully() {
        when(repository.findById(1L))
                .thenReturn(Optional.of(entity));

        when(repository.existsByCpfAndIdNot(dto.cpf(), 1L))
                .thenReturn(false);

        when(repository.save(entity)).thenReturn(entity);

        when(mapper.toResponseEntityUser(entity))
                .thenReturn(responseDTO);

        var result = service.update(1L, dto);

        assertNotNull(result);

        verify(repository).findById(1L);
        verify(repository).existsByCpfAndIdNot(dto.cpf(), 1L);
        verify(mapper).updateEntity(entity, dto);
        verify(repository).save(entity);
    }

    @Test
    void should_throw_exception_when_updating_invalid_id() {
        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(1L, dto));
    }

    @Test
    void should_disable_user_successfully() {
        when(repository.findById(1L))
                .thenReturn(Optional.of(entity));

        service.disableById(1L);

        assertFalse(entity.getActive());
        verify(repository).findById(1L);
    }

    @Test
    void should_throw_exception_when_disabling_invalid_id() {
        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.disableById(1L));
    }
}
