package com.example.estoque_api.repository;

import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.model.HistoryEntity;
import com.example.estoque_api.model.ToolEntity;
import com.example.estoque_api.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import java.util.UUID;
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
    private UUID inventoryId;

    @BeforeEach
    void setUp() {
        inventoryId = UUID.randomUUID();

        user = UserEntity.builder()
                .username("tester")
                .cpf("11144477735")
                .address("Test Street")
                .build();
        entityManager.persist(user);

        tool = ToolEntity.builder()
                .name("Test Tool")
                .active(true)
                .build();
        entityManager.persist(tool);
    }

    @Test
    @DisplayName("Should return true when a history record exists for the user and action")
    void shouldReturnTrueWhenHistoryExists() {
        HistoryEntity history = HistoryEntity.builder()
                .user(user)
                .tool(tool)
                .inventoryId(inventoryId)
                .quantityTaken(10)
                .action(InventoryAction.TAKE)
                .build();
        entityManager.persist(history);
        entityManager.flush();

        Boolean exists = repository.existsByUserAndAction(user, InventoryAction.TAKE);

        assertTrue(exists);
    }

    @Test
    @DisplayName("Should return false when no history record exists for the user and action")
    void shouldReturnFalseWhenHistoryDoesNotExist() {
        Boolean exists = repository.existsByUserAndAction(user, InventoryAction.RETURN);

        assertFalse(exists);
    }

    @Test
    @DisplayName("Should return false when record exists for the user but with a different action")
    void shouldReturnFalseWhenActionDiffers() {
        HistoryEntity history = HistoryEntity.builder()
                .user(user)
                .tool(tool)
                .inventoryId(inventoryId)
                .quantityTaken(10)
                .action(InventoryAction.TAKE)
                .build();
        entityManager.persist(history);
        entityManager.flush();

        Boolean exists = repository.existsByUserAndAction(user, InventoryAction.RETURN);

        assertFalse(exists);
    }
}