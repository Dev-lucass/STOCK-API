package com.example.estoque_api.repository;

import com.example.estoque_api.model.ToolEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ToolRepositoryTest {

    @Autowired
    private ToolRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private ToolEntity activeTool;
    private ToolEntity inactiveTool;

    @BeforeEach
    void setUp() {
        activeTool = ToolEntity.builder()
                .name("Smartphone")
                .active(true)
                .build();

        inactiveTool = ToolEntity.builder()
                .name("Notebook")
                .active(false)
                .build();

        entityManager.persist(activeTool);
        entityManager.persist(inactiveTool);
        entityManager.flush();
    }

    @Test
    @DisplayName("Should return true when a tool exists with the given name")
    void shouldReturnTrueWhenNameExists() {
        Boolean exists = repository.existsByName("Smartphone");
        assertTrue(exists);
    }

    @Test
    @DisplayName("Should return true when another tool exists with the same name excluding current ID")
    void shouldReturnTrueWhenNameExistsAndIdNot() {
        ToolEntity anotherTool = ToolEntity.builder()
                .name("Monitor")
                .active(true)
                .build();
        entityManager.persist(anotherTool);

        Boolean exists = repository.existsByNameAndIdNot("Smartphone", anotherTool.getId());
        assertTrue(exists);
    }

    @Test
    @DisplayName("Should return false when checking name existence against its own ID")
    void shouldReturnFalseWhenNameExistsButIsTheSameId() {
        Boolean exists = repository.existsByNameAndIdNot("Smartphone", activeTool.getId());
        assertFalse(exists);
    }

    @Test
    @DisplayName("Should return a list of all active tools")
    void shouldFindAllByActiveTrue() {
        List<ToolEntity> actives = repository.findAllByActiveTrue();

        assertAll(
                () -> assertEquals(1, actives.size()),
                () -> assertEquals("Smartphone", actives.getFirst().getName()),
                () -> assertTrue(actives.getFirst().getActive())
        );
    }

    @Test
    @DisplayName("Should return a list of all inactive tools")
    void shouldFindAllByActiveFalse() {
        List<ToolEntity> inactives = repository.findAllByActiveFalse();

        assertAll(
                () -> assertEquals(1, inactives.size()),
                () -> assertEquals("Notebook", inactives.get(0).getName()),
                () -> assertFalse(inactives.get(0).getActive())
        );
    }
}