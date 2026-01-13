package com.example.estoque_api.repository;

import com.example.estoque_api.model.InventoryEntity;
import com.example.estoque_api.model.ToolEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class InventoryRepositoryTest {

    @Autowired
    private InventoryRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private ToolEntity tool1;
    private ToolEntity tool2;

    @BeforeEach
    void setUp() {
        tool1 = ToolEntity.builder()
                .name("Keyboard")
                .active(true)
                .createdIn(LocalDateTime.now())
                .currentLifeCycle(100.0)
                .build();

        tool2 = ToolEntity.builder()
                .name("Mouse")
                .active(false)
                .createdIn(LocalDateTime.now())
                .currentLifeCycle(100.0)
                .build();

        entityManager.persist(tool1);
        entityManager.persist(tool2);
    }

    @Test
    void existsByTool_RecordExists_ReturnsTrue() {
        var inventory = InventoryEntity.builder()
                .tool(tool1)
                .quantityInitial(10)
                .quantityCurrent(10)
                .build();

        entityManager.persist(inventory);

        var exists = repository.existsByTool(tool1);

        assertTrue(exists);
    }

    @Test
    void existsByToolAndIdNot_OnlySelfExists_ReturnsFalse() {
        var inventory = InventoryEntity.builder()
                .tool(tool1)
                .quantityInitial(10)
                .quantityCurrent(10)
                .build();

        var saved = entityManager.persist(inventory);

        var existsAnother = repository.existsByToolIdAndIdNot(tool1.getId(), saved.getId());

        assertFalse(existsAnother);
    }

    @Test
    void existsByToolAndIdNot_ToolNotPresentInAnyOtherId_ReturnsFalse() {
        var inventory1 = InventoryEntity.builder()
                .tool(tool1)
                .quantityInitial(10)
                .quantityCurrent(10)
                .build();

        var saved1 = entityManager.persist(inventory1);

        var exists = repository.existsByToolIdAndIdNot(tool2.getId(), saved1.getId());

        assertFalse(exists);
    }

    @Test
    void findById_RecordExists_ReturnsOptionalWithEntity() {
        var inventory = InventoryEntity.builder()
                .tool(tool1)
                .quantityInitial(10)
                .quantityCurrent(5)
                .build();

        var saved = entityManager.persist(inventory);

        var found = repository.findById(saved.getId());

        assertAll(
                () -> assertTrue(found.isPresent()),
                () -> assertEquals(saved.getId(), found.get().getId())
        );
    }
}