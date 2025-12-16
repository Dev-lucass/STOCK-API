package com.example.estoque_api.service;

import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.exceptions.DuplicateResouceException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.model.ProductEntity;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.repository.InventoryEntityRepository;
import com.example.estoque_api.validation.InventoryEntityValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class InventoryEntityServiceTest {

    @Mock
    InventoryEntityRepository repository;

    @Mock
    InventoryEntityValidation validation;

    @Mock
    HistoryEntityService historyService;

    @InjectMocks
    InventoryEntityService service;

    InventoryEntity inventory;
    InventoryEntity inventoryUpdated;
    InventoryEntity order;
    ProductEntity product;
    UserEntity user;

    @BeforeEach
    void setUp() {
        product = new ProductEntity();
        product.setId(1L);

        inventory = new InventoryEntity();
        inventory.setId(1L);
        inventory.setQuantity(200);
        inventory.setProduct(product);

        inventoryUpdated = new InventoryEntity();
        inventoryUpdated.setQuantity(300);
        inventoryUpdated.setProduct(product);

        order = new InventoryEntity();
        order.setQuantity(50);
        order.setProduct(product);

        user = new UserEntity();
        user.setId(1L);
    }

    @Test
    void should_save_inventory() {
        doNothing().when(validation).validationInventoryEntityIsDuplicatedOnCreate(inventory);
        when(repository.save(inventory)).thenReturn(inventory);

        InventoryEntity result = service.save(inventory);

        assertNotNull(result);
        verify(validation).validationInventoryEntityIsDuplicatedOnCreate(inventory);
        verify(repository).save(inventory);
    }

    @Test
    void should_throw_when_saving_duplicated_inventory() {
        doThrow(new DuplicateResouceException("PRODUCT already registered in inventory"))
                .when(validation)
                .validationInventoryEntityIsDuplicatedOnCreate(inventory);

        assertThrows(DuplicateResouceException.class, () -> service.save(inventory));

        verify(repository, never()).save(any());
    }

    @Test
    void should_find_all_inventory() {
        when(repository.findAll()).thenReturn(List.of(inventory));

        List<InventoryEntity> result = service.findAll();

        assertEquals(1, result.size());
        verify(repository).findAll();
    }

    @Test
    void should_update_inventory() {
        when(validation.validationInventoryEntityIdIsValid(1L)).thenReturn(inventory);
        when(repository.save(inventory)).thenReturn(inventory);

        InventoryEntity result = service.update(1L, inventoryUpdated);

        assertEquals(300, result.getQuantity());
        verify(repository).save(inventory);
    }

    @Test
    void should_throw_when_updating_invalid_id() {
        when(validation.validationInventoryEntityIdIsValid(1L))
                .thenThrow(new ResourceNotFoundException("Invalid ID"));

        assertThrows(ResourceNotFoundException.class, () -> service.update(1L, inventoryUpdated));
    }

    @Test
    void should_delete_inventory() {
        when(validation.validationInventoryEntityIdIsValid(1L)).thenReturn(inventory);

        service.deleteById(1L);

        verify(repository).delete(inventory);
    }

    @Test
    void should_throw_when_deleting_invalid_id() {
        when(validation.validationInventoryEntityIdIsValid(1L)).thenThrow(new ResourceNotFoundException("Invalid ID"));

        assertThrows(ResourceNotFoundException.class, () -> service.deleteById(1L));

        verify(repository, never()).delete(any());
    }

    @Test
    void should_take_from_inventory() {
        when(repository.findByProduct(product)).thenReturn(Optional.of(inventory));
        when(repository.save(inventory)).thenReturn(inventory);

        InventoryEntity result = service.takeFromInventory(user, order);

        assertEquals(150, result.getQuantity());
        verify(historyService).save(user, product, InventoryAction.TAKE, 50);
        verify(repository).save(inventory);
    }

    @Test
    void should_throw_when_not_enough_stock() {
        order.setQuantity(300);
        when(repository.findByProduct(product)).thenReturn(Optional.of(inventory));

        assertThrows(ResourceNotFoundException.class, () -> service.takeFromInventory(user, order));

        verify(repository, never()).save(any());
        verify(historyService, never()).save(any(), any(), any(), anyInt());
    }

    @Test
    void should_throw_when_product_not_found_on_take() {
        when(repository.findByProduct(product)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.takeFromInventory(user, order));
    }

    @Test
    void should_return_to_inventory() {
        when(repository.findByProduct(product)).thenReturn(Optional.of(inventory));
        when(repository.save(inventory)).thenReturn(inventory);

        InventoryEntity result = service.returnFromInventory(user, order);

        assertEquals(250, result.getQuantity());
        verify(historyService).save(user, product, InventoryAction.RETURN, 50);
        verify(repository).save(inventory);
    }

    @Test
    void should_throw_when_product_not_found_on_return() {
        when(repository.findByProduct(product)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.returnFromInventory(user, order));
    }
}
