package com.example.estoque_api.repository;

import com.example.estoque_api.model.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ProductEntityRepositoryTest {

    @Autowired
    private ProductEntityRepository repository;

    private ProductEntity activeProduct;
    private ProductEntity inactiveProduct;

    @BeforeEach
    void setup() {
        activeProduct = ProductEntity.builder()
                .name("Teclado Mecânico")
                .active(true)
                .build();

        inactiveProduct = ProductEntity.builder()
                .name("Mouse Óptico")
                .active(false)
                .build();
    }

    @Test
    void should_save_product() {
        var savedProduct = repository.save(activeProduct);

        assertNotNull(savedProduct.getId());
        assertEquals("Teclado Mecânico", savedProduct.getName());
        assertTrue(savedProduct.getActive());
    }

    @Test
    void should_apply_default_active_when_null() {
        var product = ProductEntity.builder()
                .name("Monitor")
                .build();

        var savedProduct = repository.save(product);
        assertTrue(savedProduct.getActive());
    }

    @Test
    void should_find_all_active_products() {
        repository.save(activeProduct);
        repository.save(inactiveProduct);

        var result = repository.findAllByActiveTrue();

        assertEquals(1, result.size());
        assertTrue(result.getFirst().getActive());
    }

    @Test
    void should_find_all_inactive_products() {
        repository.save(activeProduct);
        repository.save(inactiveProduct);

        var result = repository.findAllByActiveFalse();

        assertEquals(1, result.size());
        assertFalse(result.getFirst().getActive());
    }

    @Test
    void should_return_true_when_name_exists() {
        repository.save(activeProduct);
        Boolean exists = repository.existsByName("Teclado Mecânico");
        assertTrue(exists);
    }

    @Test
    void should_return_false_when_name_does_not_exist() {
        Boolean exists = repository.existsByName("Produto Inexistente");

        assertFalse(exists);
    }

    @Test
    void should_return_true_when_name_exists_and_id_is_different() {
        var savedProduct = repository.save(activeProduct);

        Boolean exists = repository.existsByNameAndIdNot(
                "Teclado Mecânico",
                savedProduct.getId() + 1
        );

        assertTrue(exists);
    }

    @Test
    void should_return_false_when_name_exists_and_id_is_same() {
        var savedProduct = repository.save(activeProduct);

        Boolean exists = repository.existsByNameAndIdNot(
                "Teclado Mecânico",
                savedProduct.getId()
        );

        assertFalse(exists);
    }

    @Test
    void should_update_product() {
        var savedProduct = repository.save(activeProduct);

        savedProduct.setName("Teclado Mecânico RGB");
        savedProduct.setActive(false);

        var updatedProduct = repository.save(savedProduct);

        assertEquals(savedProduct.getId(), updatedProduct.getId());
        assertEquals("Teclado Mecânico RGB", updatedProduct.getName());
        assertFalse(updatedProduct.getActive());
    }

    @Test
    void should_delete_product_by_id() {
        var savedProduct = repository.save(activeProduct);
        repository.deleteById(savedProduct.getId());
        assertTrue(repository.findById(savedProduct.getId()).isEmpty());
    }
}
