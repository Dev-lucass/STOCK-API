package com.example.estoque_api.service;

import com.example.estoque_api.exceptions.DuplicateResouceException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.repository.UserEntityRepository;
import com.example.estoque_api.validation.UserEntityValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class UserEntityServiceTest {


    @Mock
    private UserEntityRepository repository;

    @Mock
    private UserEntityValidation validation;

    @InjectMocks
    private UserEntityService service;

    private UserEntity user;
    private UserEntity userUpdated;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setId(1L);
        user.setUsername("Lucas");
        user.setCpf("123");
        user.setAddress("Rua A");

        userUpdated = new UserEntity();
        userUpdated.setId(1L);
        userUpdated.setUsername("Lucas Updated");
        userUpdated.setCpf("456");
        userUpdated.setAddress("Rua B");
    }

    @Test
    void should_save_user_successfully() {
        doNothing().when(validation).validationUserEntityIsDuplicatedOnCreate(user);
        when(repository.save(user)).thenReturn(user);

        UserEntity result = service.save(user);

        assertNotNull(result);
        assertEquals(user, result);

        verify(validation).validationUserEntityIsDuplicatedOnCreate(user);
        verify(repository).save(user);
    }

    @Test
    void should_throw_exception_when_saving_duplicated_cpf() {
        doThrow(new DuplicateResouceException("CPF already registered"))
                .when(validation)
                .validationUserEntityIsDuplicatedOnCreate(user);

        assertThrows(DuplicateResouceException.class, () -> service.save(user));

        verify(validation).validationUserEntityIsDuplicatedOnCreate(user);
        verify(repository, never()).save(any());
    }

    @Test
    void should_return_all_users() {
        when(repository.findAll()).thenReturn(List.of(user));

        List<UserEntity> result = service.findAll();

        assertEquals(1, result.size());
        verify(repository).findAll();
    }

    @Test
    void should_update_user_successfully() {
        when(validation.validationUserEntityIdIsValid(1L)).thenReturn(user);
        doNothing().when(validation).validationUserEntityIsDuplicatedOnUpdate(userUpdated);

        UserEntity result = service.update(1L, userUpdated);

        assertEquals("Lucas Updated", result.getUsername());
        assertEquals("456", result.getCpf());
        assertEquals("Rua B", result.getAddress());

        verify(validation).validationUserEntityIdIsValid(1L);
        verify(validation).validationUserEntityIsDuplicatedOnUpdate(userUpdated);
    }

    @Test
    void should_throw_exception_when_updating_invalid_id() {
        when(validation.validationUserEntityIdIsValid(1L)).thenThrow(new ResourceNotFoundException("Invalid user ID"));
        assertThrows(ResourceNotFoundException.class, () -> service.update(1L, userUpdated));
    }

    @Test
    void should_delete_user_successfully() {
        when(validation.validationUserEntityIdIsValid(1L)).thenReturn(user);
        doNothing().when(repository).delete(user);
        service.deleteById(1L);
        verify(repository).delete(user);
    }

    @Test
    void should_throw_exception_when_deleting_invalid_id() {
        when(validation.validationUserEntityIdIsValid(1L)).thenThrow(new ResourceNotFoundException("Invalid user ID"));
        assertThrows(ResourceNotFoundException.class, () -> service.deleteById(1L));
        verify(repository, never()).delete(any());
    }
}

