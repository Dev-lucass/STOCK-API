package com.example.estoque_api.service;

import com.example.estoque_api.dto.request.ProductEntityDTO;
import com.example.estoque_api.dto.response.entity.ProductEntityResponseDTO;
import com.example.estoque_api.exceptions.DuplicateResouceException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.mapper.ProductEntityMapper;
import com.example.estoque_api.model.ProductEntity;
import com.example.estoque_api.repository.ProductEntityRepository;
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

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProductEntityServiceTest {

    @Mock
    private ProductEntityRepository repository;

    @Mock
    private ProductEntityMapper mapper;

    @InjectMocks
    private ProductEntityService service;

    private ProductEntity product;
    private ProductEntityDTO dto;
    private ProductEntityResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        product = new ProductEntity();
        product.setId(1L);
        product.setName("makita");
        product.setActive(true);

        dto = new ProductEntityDTO("makita", true);

        responseDTO = new ProductEntityResponseDTO(
                1L,
                "pen",
                true,
                LocalDate.now());
    }

    @Test
    void should_save_product_successfully() {
        when(repository.existsByName(dto.name()))
                .thenReturn(false);

        when(mapper.toEntityProduct(dto))
                .thenReturn(product);

        when(repository.save(product))
                .thenReturn(product);

        when(mapper.toResponseEntityProduct(product))
                .thenReturn(responseDTO);

        var result = service.save(dto);

        assertNotNull(result);
        assertEquals(responseDTO, result);
        verify(repository).existsByName(dto.name());
        verify(repository).save(product);
    }

    @Test
    void should_throw_exception_when_saving_duplicated_name() {
        when(repository.existsByName(dto.name()))
                .thenReturn(true);

        assertThrows(DuplicateResouceException.class, () -> service.save(dto));

        verify(repository).existsByName(dto.name());
        verify(repository, never()).save(any());
    }

    @Test
    void should_return_all_active_products() {
        when(repository.findAllByActiveTrue())
                .thenReturn(List.of(product));

        when(mapper.toResponseEntityProduct(product))
                .thenReturn(responseDTO);

        var result = service.findAllIsActive();

        assertEquals(1, result.size());
        verify(repository).findAllByActiveTrue();
    }

    @Test
    void should_return_all_inactive_products() {
        product.setActive(false);

        when(repository.findAllByActiveFalse())
                .thenReturn(List.of(product));

        when(mapper.toResponseEntityProduct(product))
                .thenReturn(responseDTO);

        var result = service.findAllIsNotActive();

        assertEquals(1, result.size());
        verify(repository).findAllByActiveFalse();
    }

    @Test
    void should_update_product_successfully() {
        when(repository.findById(1L))
                .thenReturn(Optional.of(product));

        when(repository.existsByNameAndIdNot(dto.name(), 1L)).thenReturn(false);

        when(repository.save(product))
                .thenReturn(product);

        when(mapper.toResponseEntityProduct(product))
                .thenReturn(responseDTO);

        var result = service.update(1L, dto);

        assertNotNull(result);

        verify(repository).findById(1L);
        verify(repository).existsByNameAndIdNot(dto.name(), 1L);
        verify(mapper).updateEntity(product, dto);
        verify(repository).save(product);
    }

    @Test
    void should_throw_exception_when_updating_with_duplicated_name() {
        when(repository.findById(1L))
                .thenReturn(Optional.of(product));

        when(repository.existsByNameAndIdNot(dto.name(), 1L))
                .thenReturn(true);

        assertThrows(DuplicateResouceException.class, () -> service.update(1L, dto));

        verify(repository).existsByNameAndIdNot(dto.name(), 1L);
        verify(repository, never()).save(any());
    }

    @Test
    void should_throw_exception_when_updating_invalid_id() {
        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(1L, dto));
    }

    @Test
    void should_disable_product_successfully() {
        when(repository.findById(1L))
                .thenReturn(Optional.of(product));

        service.disableById(1L);

        assertFalse(product.getActive());
        verify(repository).findById(1L);
    }

    @Test
    void should_throw_exception_when_disabling_invalid_id() {
        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.disableById(1L));
    }

    @Test
    void should_find_product_by_id_successfully() {
        when(repository.findById(1L))
                .thenReturn(Optional.of(product));

        var result = service
                .findProductByIdOrElseThrow(1L);

        assertEquals(product, result);
    }

    @Test
    void should_throw_exception_when_finding_invalid_id() {
        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findProductByIdOrElseThrow(1L));
    }
}
