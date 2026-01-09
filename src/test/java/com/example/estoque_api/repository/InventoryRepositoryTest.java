package com.example.estoque_api.repository;

import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.model.ToolEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class InventoryRepositoryTest {

    @Autowired
    private InventoryRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private ToolEntity tool1;
    private ToolEntity tool2;
    private long inventoryId;

    @BeforeEach
    void setUp() {
        tool1 = ToolEntity.builder()
                .name("Keyboard")
                .active(true)
                .build();


        tool2 = ToolEntity.builder()
                .name("Mouse")
                .active(false)
                .build();

        entityManager.persist(tool1);
        entityManager.persist(tool2);

        inventoryId = 1L;
    }

    @Test
    @DisplayName("Should return true when inventory exists for a specific tool")
    void shouldReturnTrueWhenExistsByTool() {
        var inventory = InventoryEntity.builder()
                .tool(tool1)
                .quantityInitial(10)
                .quantityCurrent(10)
                .build();

        entityManager.persist(inventory);

        var exists = repository
                .existsByTool(tool1);

        assertTrue(exists);
    }

    @Test
    @DisplayName("Should return true when another inventory exists for the same tool excluding current ID")
    void shouldReturnTrueWhenExistsByToolAndIdNot() {
        var inventory = InventoryEntity.builder()
                .tool(tool1)
                .quantityInitial(10)
                .quantityCurrent(10)
                .build();

        var saved = entityManager.persist(inventory);

        var existsItself = repository
                .existsByToolAndIdNot(tool1, saved.getId());

        assertFalse(existsItself);
    }

    @Test
    @DisplayName("Should return only inventories where associated tool is active")
    void shouldFindAllByToolActiveTrue() {
        var invActive = InventoryEntity.builder()
                .tool(tool1)
                .quantityInitial(10)
                .quantityCurrent(50)
                .build();

        var invInactive = InventoryEntity.builder()
                .tool(tool2)
                .quantityInitial(5)
                .quantityCurrent(50)
                .build();

        entityManager.persist(invActive);
        entityManager.persist(invInactive);

        var results = repository.findAllByToolActiveTrue();

        assertAll(
                () -> assertEquals(1, results.size())
        );
    }

    @Test
    @DisplayName("Should find inventory by its custom inventoryId string")
    void shouldFindByInventoryId() {

        var inventory = InventoryEntity.builder()
                .tool(tool1)
                .quantityInitial(10)
                .quantityCurrent(5)
                .build();

        entityManager.persist(inventory);

        var found = repository
                .findById(1L);

        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getId());
    }
}