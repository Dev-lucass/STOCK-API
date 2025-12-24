package com.example.estoque_api.service;

import com.example.estoque_api.dto.request.UserEntityDTO;
import com.example.estoque_api.dto.response.entity.UserEntityResponseDTO;
import com.example.estoque_api.exceptions.DuplicateResouceException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.mapper.UserEntityMapper;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.repository.UserEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserEntityServiceTest {

    @Mock
    private UserEntityRepository repository;

    @Mock
    private UserEntityMapper mapper;

    @InjectMocks
    private UserEntityService service;

    private UserEntity user;
    private UserEntityDTO userDTO;
    private UserEntityResponseDTO responseDTO;
    private final String VALID_CPF = "11144477735";

    @BeforeEach
    void setUp() {
        user = UserEntity.builder()
                .id(1L)
                .username("john_doe")
                .cpf(VALID_CPF)
                .active(true)
                .build();

        userDTO = new UserEntityDTO("john_doe", VALID_CPF, "123 Street");

        responseDTO = UserEntityResponseDTO.builder()
                .id(1L)
                .username("john_doe")
                .createdAt(LocalDate.now())
                .build();
    }

    @Test
    @DisplayName("Should save user successfully when CPF is unique")
    void shouldSaveUserSuccessfully() {
        when(repository.existsByCpf(VALID_CPF)).thenReturn(false);
        when(mapper.toEntityUser(userDTO)).thenReturn(user);
        when(repository.save(user)).thenReturn(user);
        when(mapper.toResponseEntityUser(user)).thenReturn(responseDTO);

        UserEntityResponseDTO result = service.save(userDTO);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(responseDTO.username(), result.username()),
                () -> verify(repository).existsByCpf(VALID_CPF),
                () -> verify(repository).save(any(UserEntity.class))
        );
    }

    @Test
    @DisplayName("Should throw DuplicateResouceException when saving user with existing CPF")
    void shouldThrowExceptionWhenCpfAlreadyExistsOnSave() {
        when(repository.existsByCpf(VALID_CPF)).thenReturn(true);

        assertThrows(DuplicateResouceException.class, () -> service.save(userDTO));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should return user when ID exists")
    void shouldFindUserByIdWhenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(user));

        UserEntity result = service.findUserByIdOrElseThrow(1L);

        assertEquals(user, result);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when user ID does not exist")
    void shouldThrowExceptionWhenIdDoesNotExist() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findUserByIdOrElseThrow(1L));
    }

    @Test
    @DisplayName("Should update user successfully")
    void shouldUpdateUserSuccessfully() {
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(repository.existsByCpfAndIdNot(VALID_CPF, 1L)).thenReturn(false);
        when(repository.save(user)).thenReturn(user);
        when(mapper.toResponseEntityUser(user)).thenReturn(responseDTO);

        UserEntityResponseDTO result = service.update(1L, userDTO);

        assertAll(
                () -> assertNotNull(result),
                () -> verify(mapper).updateEntity(user, userDTO),
                () -> verify(repository).save(user)
        );
    }

    @Test
    @DisplayName("Should disable user by setting active to false")
    void shouldDisableUserSuccessfully() {
        when(repository.findById(1L)).thenReturn(Optional.of(user));

        service.disableById(1L);

        assertFalse(user.getActive());
    }
}