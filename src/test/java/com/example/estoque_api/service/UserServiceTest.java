package com.example.estoque_api.service;

import com.example.estoque_api.dto.request.filter.UserFilterDTO;
import com.example.estoque_api.dto.request.persist.UserDTO;
import com.example.estoque_api.dto.response.entity.UserResponseDTO;
import com.example.estoque_api.exceptions.DuplicateResouceException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.mapper.UserMapper;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.repository.UserRepository;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @Mock
    private HistoryService historyService;

    @InjectMocks
    private UserService service;

    private UserEntity user;
    private UserDTO userDTO;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp() {
        user = UserEntity.builder()
                .id(1L)
                .username("joao_silva")
                .cpf("12345678901")
                .active(true)
                .build();

        userDTO = new UserDTO("joao_silva", "12345678901", "rua madeireira");

        userResponseDTO = UserResponseDTO.builder()
                .id(1L)
                .username("joao_silva")
                .build();
    }

    @Test
    @DisplayName("Save user success")
    void save_Success() {
        when(repository.existsByCpf(anyString())).thenReturn(false);
        when(mapper.toEntityUser(any())).thenReturn(user);
        when(repository.save(any())).thenReturn(user);
        when(mapper.toResponseEntityUser(any())).thenReturn(userResponseDTO);

        UserResponseDTO result = service.save(userDTO);

        assertThat(result).isNotNull();
        verify(repository).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Save user duplicate CPF error")
    void save_DuplicateCpfError() {
        when(repository.existsByCpf(anyString())).thenReturn(true);

        assertThatThrownBy(() -> service.save(userDTO))
                .isInstanceOf(DuplicateResouceException.class);
    }

    @Test
    @DisplayName("Update user success")
    void update_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(repository.existsByCpfAndIdNot(anyString(), eq(1L))).thenReturn(false);
        when(repository.save(any())).thenReturn(user);
        when(mapper.toResponseEntityUser(any())).thenReturn(userResponseDTO);

        UserResponseDTO result = service.update(1L, userDTO);

        assertThat(result).isNotNull();
        verify(mapper).updateEntity(eq(user), eq(userDTO));
        verify(repository).save(user);
    }

    @Test
    @DisplayName("Disable user by ID success")
    void disableById_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(historyService).validateUserWhetherUserOwes(user);

        service.disableById(1L);

        assertThat(user.getActive()).isFalse();
        verify(historyService).validateUserWhetherUserOwes(user);
    }

    @Test
    @DisplayName("Disable user should throw exception when user owes tools")
    void disableById_UserOwesError() {
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        doThrow(new RuntimeException("User owes tools")).when(historyService)
                .validateUserWhetherUserOwes(user);

        assertThatThrownBy(() -> service.disableById(1L))
                .isInstanceOf(RuntimeException.class);

        assertThat(user.getActive()).isTrue();
    }

    @Test
    @DisplayName("Find all with filter and pageable success")
    void findAll_WithFilter_Success() {
        UserFilterDTO filter = UserFilterDTO.builder().username("joao").build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserEntity> page = new PageImpl<>(List.of(user));

        when(repository.findAll(any(Predicate.class), any(Pageable.class))).thenReturn(page);

        var result = service.findAll(filter, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(repository).findAll(any(Predicate.class), any(Pageable.class));
    }

    @Test
    @DisplayName("Validate user is userActive error")
    void validateUserIsActive_Error() {
        user.setActive(false);

        assertThatThrownBy(() -> service.validateUserIsActive(user))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User is inactive and cannot perform this action.");
    }

    @Test
    @DisplayName("Find user by ID not found error")
    void findById_NotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findUserByIdOrElseThrow(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Update user not found error")
    void update_NotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(99L, userDTO))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Update user duplicate CPF on different ID error")
    void update_DuplicateCpfError() {
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(repository.existsByCpfAndIdNot(anyString(), eq(1L))).thenReturn(true);

        assertThatThrownBy(() -> service.update(1L, userDTO))
                .isInstanceOf(DuplicateResouceException.class);
    }
}