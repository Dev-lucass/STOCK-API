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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @BeforeEach
    void setUp() {
        user = UserEntity.builder()
                .id(1L)
                .username("joao_silva")
                .cpf("12345678901")
                .active(true)
                .build();

        userDTO = new UserEntityDTO("joao_silva", "11144477735", "rua madeireira");
    }

    @Test
    @DisplayName("Save user success")
    void save_Success() {
        when(repository.existsByCpf(anyString())).thenReturn(false);
        when(mapper.toEntityUser(any())).thenReturn(user);
        when(repository.save(any())).thenReturn(user);

        service.save(userDTO);

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
    @DisplayName("Find all active users success")
    void findAll_Success() {
        when(repository.findAllByActiveTrue()).thenReturn(List.of(user));

        List<UserEntityResponseDTO> result = service.findAll();

        assertThat(result).isNotNull();
        verify(repository).findAllByActiveTrue();
    }

    @Test
    @DisplayName("Update user success")
    void update_Success() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(user));
        when(repository.existsByCpfAndIdNot(anyString(), anyLong())).thenReturn(false);
        when(repository.save(any())).thenReturn(user);

        service.update(1L, userDTO);

        verify(mapper).updateEntity(eq(user), eq(userDTO));
        verify(repository).save(user);
    }

    @Test
    @DisplayName("Update user duplicate CPF error")
    void update_DuplicateCpfError() {
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(repository.existsByCpfAndIdNot(anyString(), eq(1L))).thenReturn(true);

        assertThatThrownBy(() -> service.update(1L, userDTO))
                .isInstanceOf(DuplicateResouceException.class);
    }

    @Test
    @DisplayName("Disable user by ID success")
    void disableById_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(user));

        service.disableById(1L);

        assertThat(user.getActive()).isFalse();
    }

    @Test
    @DisplayName("Find user by ID not found error")
    void findById_NotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findUserByIdOrElseThrow(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Filter by username pageable success")
    @SuppressWarnings("unchecked")
    void filterByUsernamePageable_Success() {
        Page<UserEntity> page = new PageImpl<>(List.of(user));
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<UserEntity> result = service.filterByUsernamePageable("joao", 0, 10);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(repository).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    @DisplayName("Filter by username with null name builds empty specification")
    void filterByUsernamePageable_NullName() {
        Page<UserEntity> page = new PageImpl<>(List.of(user));
        when(repository.findAll((Specification<UserEntity>) null, PageRequest.of(0, 10, org.springframework.data.domain.Sort.by("username").ascending())))
                .thenReturn(page);

        Page<UserEntity> result = service.filterByUsernamePageable(null, 0, 10);

        assertThat(result).isNotNull();
    }
}