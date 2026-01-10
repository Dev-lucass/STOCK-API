package com.example.estoque_api.repository;

import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.model.HistoryEntity;
import com.example.estoque_api.model.ToolEntity;
import com.example.estoque_api.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class HistoryRepositoryTest {

    @Autowired
    private HistoryRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private UserEntity user;
    private ToolEntity tool;
    private long inventoryId;

    @BeforeEach
    void setUp() {
        inventoryId = 1L;

        user = UserEntity.builder()
                .username("tester")
                .cpf("11144477735")
                .address("Test Street")
                .build();

        entityManager.persist(user);

        tool = ToolEntity.builder()
                .name("Test Tool")
                .active(true)
                .createdIn(LocalDateTime.now())
                .build();

        entityManager.persist(tool);
    }

    @Test
    void existsByUserAndAction_RecordExists_ReturnsTrue() {
        var history = HistoryEntity.builder()
                .user(user)
                .tool(tool)
                .inventoryId(inventoryId)
                .quantityTaken(10)
                .action(InventoryAction.TAKE)
                .build();

        entityManager.persist(history);

        var exists = repository.existsByUserAndAction(user, InventoryAction.TAKE);

        assertTrue(exists);
    }

    @Test
    void existsByUserAndAction_RecordDoesNotExist_ReturnsFalse() {
        var exists = repository.existsByUserAndAction(user, InventoryAction.RETURN);

        assertFalse(exists);
    }

    @Test
    void existsByUserAndAction_DifferentActionExists_ReturnsFalse() {
        var history = HistoryEntity.builder()
                .user(user)
                .tool(tool)
                .inventoryId(inventoryId)
                .quantityTaken(10)
                .action(InventoryAction.TAKE)
                .build();

        entityManager.persist(history);

        var exists = repository.existsByUserAndAction(user, InventoryAction.RETURN);

        assertFalse(exists);
    }

    @Test
    void findByUser_UserHasRecords_ReturnsList() {
        var history = HistoryEntity.builder()
                .user(user)
                .tool(tool)
                .inventoryId(inventoryId)
                .quantityTaken(5)
                .action(InventoryAction.TAKE)
                .build();

        entityManager.persist(history);

        var results = repository.findByUser(user);

        assertAll(
                () -> assertFalse(results.isEmpty()),
                () -> assertTrue(results.contains(history))
        );
    }
}