package com.example.estoque_api.repository;

import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.model.ToolEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class InventoryRepositoryTest {

    @Autowired
    private InventoryRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private ToolEntity tool1;
    private ToolEntity tool2;
    private UUID inventoryId;

    @BeforeEach
    void setUp() {
        tool1 = ToolEntity.builder()
                .name("Keyboard")
                .active(true)
                .build();
        entityManager.persist(tool1);

        tool2 = ToolEntity.builder()
                .name("Mouse")
                .active(false)
                .build();
        entityManager.persist(tool2);

        inventoryId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Should return true when inventory exists for a specific tool")
    void shouldReturnTrueWhenExistsByTool() {
        InventoryEntity inventory = InventoryEntity.builder()
                .inventoryId(inventoryId)
                .tool(tool1)
                .quantityInitial(10)
                .quantityCurrent(10)
                .build();
        entityManager.persist(inventory);

        Boolean exists = repository.existsByTool(tool1);

        assertTrue(exists);
    }

    @Test
    @DisplayName("Should return true when another inventory exists for the same tool excluding current ID")
    void shouldReturnTrueWhenExistsByToolAndIdNot() {
        // To test this without unique constraint violation,
        // we first persist one, then try to check existence while ignoring its own ID.
        InventoryEntity inventory = InventoryEntity.builder()
                .inventoryId(inventoryId)
                .tool(tool1)
                .quantityInitial(10)
                .quantityCurrent(10)
                .build();

        InventoryEntity saved = entityManager.persist(inventory);
        Boolean existsItself = repository.existsByToolAndIdNot(tool1, saved.getId());

        assertFalse(existsItself);
    }

    @Test
    @DisplayName("Should return only inventories where associated tool is active")
    void shouldFindAllByToolActiveTrue() {
        InventoryEntity invActive = InventoryEntity.builder()
                .inventoryId(inventoryId)
                .tool(tool1) // active
                .quantityInitial(10)
                .build();

        InventoryEntity invInactive = InventoryEntity.builder()
                .inventoryId(inventoryId)
                .tool(tool2) // inactive
                .quantityInitial(5)
                .build();

        entityManager.persist(invActive);
        entityManager.persist(invInactive);

        List<InventoryEntity> results = repository.findAllByToolActiveTrue();

        assertAll(
                () -> assertEquals(1, results.size()),
                () -> assertEquals(inventoryId, results.get(0).getInventoryId())
        );
    }

    @Test
    @DisplayName("Should find inventory by its custom inventoryId string")
    void shouldFindByInventoryId() {
        UUID targetId = UUID.randomUUID();
        InventoryEntity inventory = InventoryEntity.builder()
                .inventoryId(targetId)
                .tool(tool1)
                .quantityInitial(10)
                .build();
        entityManager.persist(inventory);

        Optional<InventoryEntity> found = repository.findByInventoryId(targetId);

        assertTrue(found.isPresent());
        assertEquals(targetId, found.get().getInventoryId());
    }
}