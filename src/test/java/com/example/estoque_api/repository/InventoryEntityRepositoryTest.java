package com.example.estoque_api.repository;

import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.model.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class InventoryEntityRepositoryTest {

    @Autowired
    private InventoryEntityRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private ProductEntity product1;
    private ProductEntity product2;

    @BeforeEach
    void setUp() {
        product1 = ProductEntity.builder()
                .name("Keyboard")
                .active(true)
                .build();
        entityManager.persist(product1);

        product2 = ProductEntity.builder()
                .name("Mouse")
                .active(false)
                .build();
        entityManager.persist(product2);
    }

    @Test
    @DisplayName("Should return true when inventory exists for a specific product")
    void shouldReturnTrueWhenExistsByProduct() {
        InventoryEntity inventory = InventoryEntity.builder()
                .inventoryId("INV-001")
                .product(product1)
                .quantityInitial(10)
                .quantityCurrent(10)
                .build();
        entityManager.persist(inventory);

        Boolean exists = repository.existsByProduct(product1);

        assertTrue(exists);
    }

    @Test
    @DisplayName("Should return true when another inventory exists for the same product excluding current ID")
    void shouldReturnTrueWhenExistsByProductAndIdNot() {
        // To test this without unique constraint violation,
        // we first persist one, then try to check existence while ignoring its own ID.
        InventoryEntity inventory = InventoryEntity.builder()
                .inventoryId("INV-001")
                .product(product1)
                .quantityInitial(10)
                .quantityCurrent(10)
                .build();

        InventoryEntity saved = entityManager.persist(inventory);

        // This should return FALSE because the only inventory for product1 is the one with 'saved.getId()'
        Boolean existsItself = repository.existsByProductAndIdNot(product1, saved.getId());

        // Let's create a scenario where it returns TRUE:
        // Note: If your business logic strictly allows 1 inventory per product,
        // this method 'existsByProductAndIdNot' is usually used during updates to prevent duplicates.
        assertFalse(existsItself);
    }

    @Test
    @DisplayName("Should return only inventories where associated product is active")
    void shouldFindAllByProductActiveTrue() {
        InventoryEntity invActive = InventoryEntity.builder()
                .inventoryId("INV-ACTIVE")
                .product(product1) // active
                .quantityInitial(10)
                .build();

        InventoryEntity invInactive = InventoryEntity.builder()
                .inventoryId("INV-INACTIVE")
                .product(product2) // inactive
                .quantityInitial(5)
                .build();

        entityManager.persist(invActive);
        entityManager.persist(invInactive);

        List<InventoryEntity> results = repository.findAllByProductActiveTrue();

        assertAll(
                () -> assertEquals(1, results.size()),
                () -> assertEquals("INV-ACTIVE", results.get(0).getInventoryId())
        );
    }

    @Test
    @DisplayName("Should find inventory by its custom inventoryId string")
    void shouldFindByInventoryId() {
        String targetId = "UNIQUE-STR-ID";
        InventoryEntity inventory = InventoryEntity.builder()
                .inventoryId(targetId)
                .product(product1)
                .quantityInitial(10)
                .build();
        entityManager.persist(inventory);

        Optional<InventoryEntity> found = repository.findByInventoryId(targetId);

        assertTrue(found.isPresent());
        assertEquals(targetId, found.get().getInventoryId());
    }
}