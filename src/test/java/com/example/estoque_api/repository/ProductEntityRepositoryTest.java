package com.example.estoque_api.repository;

import com.example.estoque_api.model.ProductEntity;
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
class ProductEntityRepositoryTest {

    @Autowired
    private ProductEntityRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private ProductEntity activeProduct;
    private ProductEntity inactiveProduct;

    @BeforeEach
    void setUp() {
        activeProduct = ProductEntity.builder()
                .name("Smartphone")
                .active(true)
                .build();

        inactiveProduct = ProductEntity.builder()
                .name("Notebook")
                .active(false)
                .build();

        entityManager.persist(activeProduct);
        entityManager.persist(inactiveProduct);
        entityManager.flush();
    }

    @Test
    @DisplayName("Should return true when a product exists with the given name")
    void shouldReturnTrueWhenNameExists() {
        Boolean exists = repository.existsByName("Smartphone");
        assertTrue(exists);
    }

    @Test
    @DisplayName("Should return true when another product exists with the same name excluding current ID")
    void shouldReturnTrueWhenNameExistsAndIdNot() {
        ProductEntity anotherProduct = ProductEntity.builder()
                .name("Monitor")
                .active(true)
                .build();
        entityManager.persist(anotherProduct);

        Boolean exists = repository.existsByNameAndIdNot("Smartphone", anotherProduct.getId());
        assertTrue(exists);
    }

    @Test
    @DisplayName("Should return false when checking name existence against its own ID")
    void shouldReturnFalseWhenNameExistsButIsTheSameId() {
        Boolean exists = repository.existsByNameAndIdNot("Smartphone", activeProduct.getId());
        assertFalse(exists);
    }

    @Test
    @DisplayName("Should return a list of all active products")
    void shouldFindAllByActiveTrue() {
        List<ProductEntity> actives = repository.findAllByActiveTrue();

        assertAll(
                () -> assertEquals(1, actives.size()),
                () -> assertEquals("Smartphone", actives.get(0).getName()),
                () -> assertTrue(actives.get(0).getActive())
        );
    }

    @Test
    @DisplayName("Should return a list of all inactive products")
    void shouldFindAllByActiveFalse() {
        List<ProductEntity> inactives = repository.findAllByActiveFalse();

        assertAll(
                () -> assertEquals(1, inactives.size()),
                () -> assertEquals("Notebook", inactives.get(0).getName()),
                () -> assertFalse(inactives.get(0).getActive())
        );
    }
}