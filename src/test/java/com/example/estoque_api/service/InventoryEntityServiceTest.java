package com.example.estoque_api.service;

import com.example.estoque_api.dto.internal.HistoryEntityDTO;
import com.example.estoque_api.dto.request.InventoryEntityDTO;
import com.example.estoque_api.dto.request.TakeFromInventory;
import com.example.estoque_api.dto.response.entity.InventoryEntityResponseDTO;
import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.exceptions.DuplicateResouceException;
import com.example.estoque_api.exceptions.ErrorReturnToInventoryResponseException;
import com.example.estoque_api.exceptions.InvalidQuantityException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.mapper.InventoryEntityMapper;
import com.example.estoque_api.model.HistoryEntity;
import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.model.ProductEntity;
import com.example.estoque_api.model.UserEntity;
import com.example.estoque_api.repository.InventoryEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryEntityServiceTest {

    @InjectMocks
    private InventoryEntityService service;

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

    private ProductEntity product;
    private UserEntity user;
    private InventoryEntity inventory;
    private InventoryEntityDTO inventoryDTO;
    private InventoryEntityResponseDTO inventoryResponse;
    private TakeFromInventory takeRequest;

    @BeforeEach
    void setup() {
        product = ProductEntity.builder().id(1L).build();
        user = UserEntity.builder().id(1L).build();
        inventory = InventoryEntity.builder().id(1L).quantity(10).product(product).build();
        inventoryDTO = new InventoryEntityDTO(10, 1L);
        inventoryResponse = InventoryEntityResponseDTO.builder().id(1L).build();
        takeRequest = new TakeFromInventory(1L, 5, 1L);
    }

    @Test
    void should_save_inventory_when_data_is_valid() {
        when(productService.findProductByIdOrElseThrow(1L))
                .thenReturn(product);

        when(repository.existsByProduct(product))
                .thenReturn(false);

        when(mapper.toEntityInventory(inventoryDTO, product))
                .thenReturn(inventory);

        when(repository.save(inventory))
                .thenReturn(inventory);

        when(mapper.toResponseEntityInventory(inventory))
                .thenReturn(inventoryResponse);

        var result = service.save(inventoryDTO);

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    void should_throw_exception_when_product_already_exists_on_save() {
        when(productService.findProductByIdOrElseThrow(1L))
                .thenReturn(product);

        when(repository.existsByProduct(product))
                .thenReturn(true);

        assertThrows(DuplicateResouceException.class, () -> service.save(inventoryDTO));
    }

    @Test
    void should_return_list_of_active_inventory_products() {
        when(repository.findAllByProductActiveTrue())
                .thenReturn(List.of(inventory));

        when(mapper.toResponseEntityInventory(inventory))
                .thenReturn(inventoryResponse);

        var result = service.findAllByProductIsActive();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void should_update_inventory_when_valid_id_and_dto() {
        when(repository.findById(1L))
                .thenReturn(Optional.of(inventory));

        when(productService.findProductByIdOrElseThrow(1L))
                .thenReturn(product);

        when(repository.existsByProductAndIdNot(product, 1L))
                .thenReturn(false);

        when(repository.save(inventory))
                .thenReturn(inventory);

        when(mapper.toResponseEntityInventory(inventory))
                .thenReturn(inventoryResponse);

        var result = service.update(1L, inventoryDTO);

        assertNotNull(result);
        verify(mapper).updateEntity(inventory, inventoryDTO, product);
    }

    @Test
    void should_throw_exception_when_product_conflicts_on_update() {
        when(repository.findById(1L))
                .thenReturn(Optional.of(inventory));

        when(productService.findProductByIdOrElseThrow(1L))
                .thenReturn(product);

        when(repository.existsByProductAndIdNot(product, 1L))
                .thenReturn(true);

        assertThrows(DuplicateResouceException.class, () -> service.update(1L, inventoryDTO));
    }

    @Test
    void should_throw_exception_when_inventory_id_not_found_on_update() {
        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(1L, inventoryDTO));
    }

    @Test
    void should_decrease_quantity_and_save_history_on_take() {
        var historyDto = HistoryEntityDTO.builder().build();

        when(userService.findUserByIdOrElseThrow(1L))
                .thenReturn(user);

        when(productService.findProductByIdOrElseThrow(1L))
                .thenReturn(product);

        when(repository.findByProduct(product))
                .thenReturn(Optional.of(inventory));

        when(mapper.toHistoryEntityDTO(5, user, product, InventoryAction.TAKE))
                .thenReturn(historyDto);

        service.takeFromInventory(takeRequest);

        assertEquals(5, inventory.getQuantity());
        verify(historyService).save(historyDto);
    }

    @Test
    void should_throw_exception_when_insufficient_stock_on_take() {
        var largeRequest = new TakeFromInventory(1L, 20, 1L);

        when(userService.findUserByIdOrElseThrow(1L))
                .thenReturn(user);

        when(productService.findProductByIdOrElseThrow(1L))
                .thenReturn(product);

        when(repository.findByProduct(product))
                .thenReturn(Optional.of(inventory));

        assertThrows(InvalidQuantityException.class, () -> service.takeFromInventory(largeRequest));
    }

    @Test
    void should_increase_quantity_and_save_history_on_return() {
        var returnRequest = new TakeFromInventory(1L, 3, 1L);
        var historyTake = HistoryEntity.builder()
                .product(product).action(InventoryAction.TAKE).quantity(5).build();
        var historyDto = HistoryEntityDTO.builder().build();

        when(userService.findUserByIdOrElseThrow(1L))
                .thenReturn(user);

        when(productService.findProductByIdOrElseThrow(1L))
                .thenReturn(product);

        when(repository.findByProduct(product))
                .thenReturn(Optional.of(inventory));

        when(historyService.findByUser(user))
                .thenReturn(Optional.of(historyTake));

        when(mapper.toHistoryEntityDTO(3, user, product, InventoryAction.RETURN))
                .thenReturn(historyDto);

        service.returnFromInventory(returnRequest);

        assertEquals(13, inventory.getQuantity());
        verify(historyService).save(historyDto);
    }

    @Test
    void should_throw_exception_when_user_has_no_take_history_for_product() {
        var historyOtherProduct = HistoryEntity.builder()
                .product(ProductEntity.builder().id(99L).build())
                .action(InventoryAction.TAKE).quantity(5).build();

        when(userService.findUserByIdOrElseThrow(1L))
                .thenReturn(user);

        when(productService.findProductByIdOrElseThrow(1L))
                .thenReturn(product);

        when(repository.findByProduct(product))
                .thenReturn(Optional.of(inventory));

        when(historyService.findByUser(user))
                .thenReturn(Optional.of(historyOtherProduct));

        assertThrows(ErrorReturnToInventoryResponseException.class, () -> service.returnFromInventory(takeRequest));
    }

    @Test
    void should_throw_exception_when_return_quantity_exceeds_available() {
        var highReturnRequest = new TakeFromInventory(1L, 15, 1L);
        var historyTake = HistoryEntity.builder()
                .product(product).action(InventoryAction.TAKE).quantity(20).build();

        when(userService.findUserByIdOrElseThrow(1L))
                .thenReturn(user);

        when(productService.findProductByIdOrElseThrow(1L))
                .thenReturn(product);

        when(repository.findByProduct(product))
                .thenReturn(Optional.of(inventory));

        when(historyService.findByUser(user))
                .thenReturn(Optional.of(historyTake));

        assertThrows(InvalidQuantityException.class, () -> service.returnFromInventory(highReturnRequest));
    }

    @Test
    void should_throw_exception_when_product_not_in_inventory_on_take_or_return() {
        when(userService.findUserByIdOrElseThrow(1L))
                .thenReturn(user);

        when(productService.findProductByIdOrElseThrow(1L))
                .thenReturn(product);

        when(repository.findByProduct(product))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.takeFromInventory(takeRequest));
    }
}