package com.example.estoque_api.repository;

import com.example.estoque_api.model.ToolEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.junit.jupiter.api.Assertions.*;

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
                .build();

        var inactiveTool = ToolEntity.builder()
                .name("Notebook")
                .active(false)
                .build();

        entityManager.persist(activeTool);
        entityManager.persist(inactiveTool);
    }

    @Test
    @DisplayName("Should return true when a tool exists with the given name")
    void shouldReturnTrueWhenNameExists() {
        var exists = repository
                .existsByName("Smartphone");

        assertTrue(exists);
    }

    @Test
    @DisplayName("Should return true when another tool exists with the same name excluding current ID")
    void shouldReturnTrueWhenNameExistsAndIdNot() {
        var anotherTool = ToolEntity.builder()
                .name("Monitor")
                .active(true)
                .build();

        entityManager.persist(anotherTool);

        var exists = repository
                .existsByNameAndIdNot("Smartphone", anotherTool.getId());
        assertTrue(exists);
    }

    @Test
    @DisplayName("Should return false when checking name existence against its own ID")
    void shouldReturnFalseWhenNameExistsButIsTheSameId() {
        var exists = repository
                .existsByNameAndIdNot("Smartphone", activeTool.getId());

        assertFalse(exists);
    }

    @Test
    @DisplayName("Should return a list of all active tools")
    void shouldFindAllByActiveTrue() {
        var actives = repository
                .findAllByActiveTrue();

        assertAll(
                () -> assertEquals(1, actives.size()),
                () -> assertEquals("Smartphone", actives.getFirst().getName()),
                () -> assertTrue(actives.getFirst().getActive())
        );
    }

    @Test
    @DisplayName("Should return a list of all inactive tools")
    void shouldFindAllByActiveFalse() {
        var inactives = repository
                .findAllByActiveFalse();

        assertAll(
                () -> assertEquals(1, inactives.size()),
                () -> assertEquals("Notebook", inactives.getFirst().getName()),
                () -> assertFalse(inactives.getFirst().getActive())
        );
    }
}