package com.example.estoque_api.service;

import com.example.estoque_api.dto.request.ProductEntityDTO;
import com.example.estoque_api.dto.response.entity.ProductEntityResponseDTO;
import com.example.estoque_api.exceptions.DuplicateResouceException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.mapper.ProductEntityMapper;
import com.example.estoque_api.model.ProductEntity;
import com.example.estoque_api.repository.ProductEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
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
class ProductEntityServiceTest {

    @Mock
    private ProductEntityRepository repository;

    @Mock
    private ProductEntityMapper mapper;

    @InjectMocks
    private ProductEntityService service;

    private ProductEntity product;
    private ProductEntityDTO productDTO;
    private ProductEntityResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        product = ProductEntity.builder()
                .id(1L)
                .name("Smartphone")
                .active(true)
                .build();

        productDTO = new ProductEntityDTO("Smartphone", true);

        responseDTO = ProductEntityResponseDTO.builder()
                .id(1L)
                .name("Smartphone")
                .active(true)
                .createdAt(LocalDate.now())
                .build();
    }

    @Test
    @DisplayName("Should save product successfully when name is unique")
    void shouldSaveProductSuccessfully() {
        when(repository.existsByName(productDTO.name())).thenReturn(false);
        when(mapper.toEntityProduct(productDTO)).thenReturn(product);
        when(repository.save(product)).thenReturn(product);
        when(mapper.toResponseEntityProduct(product)).thenReturn(responseDTO);

        ProductEntityResponseDTO result = service.save(productDTO);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(responseDTO.name(), result.name()),
                () -> verify(repository).existsByName(productDTO.name()),
                () -> verify(repository).save(any(ProductEntity.class))
        );
    }

    @Test
    @DisplayName("Should throw DuplicateResouceException when saving product with existing name")
    void shouldThrowExceptionWhenNameAlreadyExistsOnSave() {
        when(repository.existsByName(productDTO.name())).thenReturn(true);

        assertThrows(DuplicateResouceException.class, () -> service.save(productDTO));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should find all active products")
    void shouldFindAllActiveProducts() {
        when(repository.findAllByActiveTrue()).thenReturn(List.of(product));
        when(mapper.toResponseEntityProduct(product)).thenReturn(responseDTO);

        List<ProductEntityResponseDTO> results = service.findAllIsActive();

        assertAll(
                () -> assertFalse(results.isEmpty()),
                () -> assertEquals(1, results.size()),
                () -> verify(repository).findAllByActiveTrue()
        );
    }

    @Test
    @DisplayName("Should update product successfully")
    void shouldUpdateProductSuccessfully() {
        when(repository.findById(1L)).thenReturn(Optional.of(product));
        when(repository.existsByNameAndIdNot(productDTO.name(), 1L)).thenReturn(false);
        when(repository.save(product)).thenReturn(product);
        when(mapper.toResponseEntityProduct(product)).thenReturn(responseDTO);

        ProductEntityResponseDTO result = service.update(1L, productDTO);

        assertAll(
                () -> assertNotNull(result),
                () -> verify(mapper).updateEntity(product, productDTO),
                () -> verify(repository).save(product)
        );
    }

    @Test
    @DisplayName("Should disable product by setting active to false")
    void shouldDisableProductSuccessfully() {
        when(repository.findById(1L)).thenReturn(Optional.of(product));

        service.disableById(1L);

        assertFalse(product.getActive());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when product ID is invalid")
    void shouldThrowExceptionWhenIdDoesNotExist() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findProductByIdOrElseThrow(1L));
    }
}