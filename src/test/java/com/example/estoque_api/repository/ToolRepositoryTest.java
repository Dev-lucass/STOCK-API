package com.example.estoque_api.repository;

import com.example.estoque_api.model.ToolEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ToolRepositoryTest {

    @Autowired
    private ToolRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private ToolEntity activeTool;

    @BeforeEach
    void setUp() {
        activeTool = ToolEntity.builder()
                .name("Smartphone")
                .active(true)
                .createdIn(LocalDateTime.now())
                .currentLifeCycle(100.0)
                .build();

        var inactiveTool = ToolEntity.builder()
                .name("Notebook")
                .active(false)
                .createdIn(LocalDateTime.now())
                .currentLifeCycle(100.0)
                .build();

        entityManager.persist(activeTool);
        entityManager.persist(inactiveTool);
    }

    @Test
    void existsByName_RecordExists_ReturnsTrue() {
        var exists = repository.existsByName("Smartphone");
        assertTrue(exists);
    }

    @Test
    void existsByNameAndIdNot_AnotherToolExistsWithSameName_ReturnsTrue() {
        var anotherTool = ToolEntity.builder()
                .name("Monitor")
                .active(true)
                .createdIn(LocalDateTime.now())
                .build();

        entityManager.persist(anotherTool);

        var exists = repository.existsByNameAndIdNot("Smartphone", anotherTool.getId());
        assertTrue(exists);
    }

    @Test
    void existsByNameAndIdNot_CheckingAgainstSameId_ReturnsFalse() {
        var exists = repository.existsByNameAndIdNot("Smartphone", activeTool.getId());
        assertFalse(exists);
    }

    @Test
    void existsByIdAndActiveFalse_ToolIsInactive_ReturnsTrue() {
        var inactive = ToolEntity.builder()
                .name("Tablet")
                .active(false)
                .createdIn(LocalDateTime.now())
                .build();

        var saved = entityManager.persist(inactive);

        var exists = repository.existsByIdAndActiveFalse(saved.getId());
        assertTrue(exists);
    }
}