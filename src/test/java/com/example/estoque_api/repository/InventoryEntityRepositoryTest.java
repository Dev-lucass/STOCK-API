package com.example.estoque_api.repository;

import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.model.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class InventoryEntityRepositoryTest {

    @Autowired
    private InventoryEntityRepository inventoryRepository;

    @Autowired
    private ProductEntityRepository productRepository;

    private ProductEntity activeProduct;
    private ProductEntity inactiveProduct;

    @BeforeEach
    void setup() {
        activeProduct = ProductEntity.builder()
                .name("Notebook")
                .active(true)
                .build();

        inactiveProduct = ProductEntity.builder()
                .name("Impressora")
                .active(false)
                .build();

        activeProduct = productRepository.save(activeProduct);
        inactiveProduct = productRepository.save(inactiveProduct);
    }

    @Test
    void should_save_inventory() {
        var inventory = InventoryEntity.builder()
                .product(activeProduct)
                .quantity(10)
                .build();

        var savedInventory = inventoryRepository.save(inventory);

        assertNotNull(savedInventory.getId());
        assertEquals(10, savedInventory.getQuantity());
        assertEquals(activeProduct.getId(), savedInventory.getProduct().getId());
    }

    @Test
    void should_return_true_when_inventory_exists_by_product() {
        var inventory = InventoryEntity.builder()
                .product(activeProduct)
                .quantity(5)
                .build();

        inventoryRepository.save(inventory);

        Boolean exists = inventoryRepository.existsByProduct(activeProduct);

        assertTrue(exists);
    }

    @Test
    void should_return_false_when_inventory_does_not_exist_by_product() {
        Boolean exists = inventoryRepository.existsByProduct(activeProduct);

        assertFalse(exists);
    }

    @Test
    void should_return_true_when_inventory_exists_and_id_is_different() {
        var inventory = InventoryEntity.builder()
                .product(activeProduct)
                .quantity(8)
                .build();

        var savedInventory = inventoryRepository.save(inventory);

        Boolean exists = inventoryRepository.existsByProductAndIdNot(
                activeProduct,
                savedInventory.getId() + 1
        );

        assertTrue(exists);
    }

    @Test
    void should_return_false_when_inventory_exists_and_id_is_same() {
        var inventory = InventoryEntity.builder()
                .product(activeProduct)
                .quantity(8)
                .build();

        var savedInventory = inventoryRepository.save(inventory);

        Boolean exists = inventoryRepository.existsByProductAndIdNot(
                activeProduct,
                savedInventory.getId()
        );

        assertFalse(exists);
    }

    @Test
    void should_find_inventory_by_product() {
        var inventory = InventoryEntity.builder()
                .product(activeProduct)
                .quantity(12)
                .build();

        inventoryRepository.save(inventory);

        var result = inventoryRepository.findByProduct(activeProduct);

        assertTrue(result.isPresent());
        assertEquals(12, result.get().getQuantity());
    }

    @Test
    void should_find_all_inventory_with_active_products() {
        inventoryRepository.save(
                InventoryEntity.builder()
                        .product(activeProduct)
                        .quantity(3)
                        .build()
        );

        inventoryRepository.save(
                InventoryEntity.builder()
                        .product(inactiveProduct)
                        .quantity(7)
                        .build()
        );

        var result = inventoryRepository.findAllByProductActiveTrue();

        assertEquals(1, result.size());
        assertTrue(result.getFirst().getProduct().getActive());
    }

    @Test
    void should_update_inventory_quantity() {
        var inventory = InventoryEntity.builder()
                .product(activeProduct)
                .quantity(4)
                .build();

        var savedInventory = inventoryRepository.save(inventory);

        savedInventory.setQuantity(20);

        var updatedInventory = inventoryRepository.save(savedInventory);

        assertEquals(savedInventory.getId(), updatedInventory.getId());
        assertEquals(20, updatedInventory.getQuantity());
    }

    @Test
    void should_delete_inventory_by_id() {
        var inventory = InventoryEntity.builder()
                .product(activeProduct)
                .quantity(6)
                .build();

        var savedInventory = inventoryRepository.save(inventory);
        inventoryRepository.deleteById(savedInventory.getId());
        assertTrue(inventoryRepository.findById(savedInventory.getId()).isEmpty());
    }
}
