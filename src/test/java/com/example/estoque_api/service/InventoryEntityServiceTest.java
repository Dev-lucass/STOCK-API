package com.example.estoque_api.service;

import com.example.estoque_api.dto.internal.HistoryEntityDTO;
import com.example.estoque_api.dto.request.InventoryEntityDTO;
import com.example.estoque_api.dto.request.TakeFromInventory;
import com.example.estoque_api.dto.response.entity.InventoryEntityResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryEntityReturnResponseDTO;
import com.example.estoque_api.dto.response.entity.InventoryEntityTakeResponseDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.exceptions.DuplicateResouceException;
import com.example.estoque_api.exceptions.InvalidQuantityException;
import com.example.estoque_api.exceptions.QuantitySoldOutException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.mapper.InventoryEntityMapper;
import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.model.ProductEntity;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.repository.InventoryEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryEntityServiceTest {

    @Mock
    private InventoryEntityRepository repository;
    @Mock
    private InventoryEntityMapper mapper;
    @Mock
    private ProductEntityService productService;
    @Mock
    private UserEntityService userService;
    @Mock
    private HistoryEntityService historyService;

    @InjectMocks
    private InventoryEntityService service;

    private InventoryEntity inventory;
    private ProductEntity product;
    private UserEntity user;
    private InventoryEntityDTO inventoryDTO;

    @BeforeEach
    void setUp() {
        product = ProductEntity.builder().id(1L).name("Laptop").active(true).build();
        user = UserEntity.builder().id(1L).username("worker").build();
        
        inventory = InventoryEntity.builder()
                .id(1L)
                .inventoryId("uuid-123")
                .product(product)
                .quantityInitial(100)
                .quantityCurrent(100)
                .build();

        inventoryDTO = new InventoryEntityDTO(100, 1L);
    }

    @Test
    @DisplayName("Should save inventory successfully when product is not duplicated")
    void shouldSaveInventorySuccessfully() {
        when(productService.findProductByIdOrElseThrow(1L)).thenReturn(product);
        when(repository.existsByProduct(product)).thenReturn(false);
        when(mapper.toEntityInventory(any(), any())).thenReturn(inventory);
        when(repository.save(any())).thenReturn(inventory);
        when(mapper.toResponseEntityInventory(any())).thenReturn(mock(InventoryEntityResponseDTO.class));

        service.save(inventoryDTO);

        verify(repository).save(any(InventoryEntity.class));
    }

    @Test
    @DisplayName("Should throw DuplicateResouceException when product already has inventory")
    void shouldThrowExceptionWhenProductAlreadyInInventory() {
        when(productService.findProductByIdOrElseThrow(1L)).thenReturn(product);
        when(repository.existsByProduct(product)).thenReturn(true);

        assertThrows(DuplicateResouceException.class, () -> service.save(inventoryDTO));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should decrease quantity when taking items from inventory")
    void shouldTakeFromInventorySuccessfully() {
        TakeFromInventory takeDTO = new TakeFromInventory(1L, "uuid-123", 20, 20);
        
        when(repository.findByInventoryId("uuid-123")).thenReturn(Optional.of(inventory));
        when(userService.findUserByIdOrElseThrow(1L)).thenReturn(user);
        when(mapper.buildHistoryDto(anyInt(), any(), any(), any(), anyString())).thenReturn(mock(HistoryEntityDTO.class));

        InventoryEntityTakeResponseDTO result = service.takeFromInventory(takeDTO);

        assertEquals(80, inventory.getQuantityCurrent());
        verify(repository).save(inventory);
        verify(historyService).save(any());
    }

    @Test
    @DisplayName("Should throw QuantitySoldOutException when inventory is empty")
    void shouldThrowExceptionWhenQuantityIsSoldOut() {
        TakeFromInventory takeDTO = new TakeFromInventory(1L, "uuid-123", 10, 10);
        inventory.setQuantityCurrent(0);

        when(repository.findByInventoryId("uuid-123")).thenReturn(Optional.of(inventory));
        when(userService.findUserByIdOrElseThrow(1L)).thenReturn(user);

        assertThrows(QuantitySoldOutException.class, () -> service.takeFromInventory(takeDTO));
    }

    @Test
    @DisplayName("Should throw InvalidQuantityException when quantity is zero or negative")
    void shouldThrowExceptionWhenQuantityIsInvalid() {
        TakeFromInventory takeDTO = new TakeFromInventory(1L, "uuid-123", 0, 0);

        when(repository.findByInventoryId("uuid-123")).thenReturn(Optional.of(inventory));
        when(userService.findUserByIdOrElseThrow(1L)).thenReturn(user);

        assertThrows(InvalidQuantityException.class, () -> service.takeFromInventory(takeDTO));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when user tries to return items they never took")
    void shouldThrowExceptionWhenUserNeverTookItems() {
        TakeFromInventory returnDTO = new TakeFromInventory(1L, "uuid-123", 10, 10);
        
        when(repository.findByInventoryId("uuid-123")).thenReturn(Optional.of(inventory));
        when(userService.findUserByIdOrElseThrow(1L)).thenReturn(user);
        when(historyService.validateUserTakedFromInventory(user, InventoryAction.TAKE)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.returnFromInventory(returnDTO));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when inventory ID is not found")
    void shouldThrowExceptionWhenInventoryIdNotFound() {
        when(repository.findByInventoryId("invalid-id")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.takeFromInventory(new TakeFromInventory(1L, "invalid-id", 10, 10)));
    }
}