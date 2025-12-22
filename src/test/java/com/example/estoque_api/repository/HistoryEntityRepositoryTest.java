package com.example.estoque_api.repository;

import com.example.estoque_api.enums.InventoryAction;
import com.example.estoque_api.model.HistoryEntity;
import com.example.estoque_api.model.HistoryId;
import com.example.estoque_api.model.ProductEntity;
import com.example.estoque_api.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class HistoryEntityRepositoryTest {

    @Autowired
    private HistoryEntityRepository historyRepository;

    @Autowired
    private UserEntityRepository userRepository;

    @Autowired
    private ProductEntityRepository productRepository;

    private UserEntity user;
    private ProductEntity product;
    private HistoryId historyId;

    @BeforeEach
    void setup() {
        user = userRepository.save(
                UserEntity.builder()
                        .username("Lucas Silva")
                        .cpf("11144477735")
                        .address("Rua das Flores, 123")
                        .active(true)
                        .build()
        );

        product = productRepository.save(
                ProductEntity.builder()
                        .name("Notebook")
                        .active(true)
                        .build()
        );

        historyId = HistoryId.builder()
                .userId(user.getId())
                .productId(product.getId())
                .createdAt(LocalDate.now())
                .build();
    }

    @Test
    void should_save_history() {
        var history = HistoryEntity.builder()
                .id(historyId)
                .user(user)
                .product(product)
                .action(InventoryAction.TAKE)
                .quantity(10)
                .build();

        var savedHistory = historyRepository.save(history);

        assertNotNull(savedHistory);
        assertEquals(InventoryAction.TAKE, savedHistory.getAction());
        assertEquals(10, savedHistory.getQuantity());
        assertEquals(user.getId(), savedHistory.getUser().getId());
        assertEquals(product.getId(), savedHistory.getProduct().getId());
    }

    @Test
    void should_find_history_by_user() {
        historyRepository.save(
                HistoryEntity.builder()
                        .id(historyId)
                        .user(user)
                        .product(product)
                        .action(InventoryAction.RETURN)
                        .quantity(3)
                        .build()
        );

        var result = historyRepository.findByUser(user);

        assertTrue(result.isPresent());
        assertEquals(InventoryAction.RETURN, result.get().getAction());
    }

    @Test
    void should_update_history_quantity() {
        var history = historyRepository.save(
                HistoryEntity.builder()
                        .id(historyId)
                        .user(user)
                        .product(product)
                        .action(InventoryAction.TAKE)
                        .quantity(5)
                        .build()
        );

        history.setQuantity(20);
        var updatedHistory = historyRepository.save(history);
        assertEquals(20, updatedHistory.getQuantity());
    }

    @Test
    void should_delete_history_by_id() {
        var history = historyRepository.save(
                HistoryEntity.builder()
                        .id(historyId)
                        .user(user)
                        .product(product)
                        .action(InventoryAction.TAKE)
                        .quantity(2)
                        .build()
        );

        historyRepository.deleteById(history.getId());
        assertTrue(historyRepository.findById(history.getId()).isEmpty());
    }
}
