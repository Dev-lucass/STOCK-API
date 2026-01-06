package com.example.estoque_api.repository;

import com.example.estoque_api.model.UserEntity;
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
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private UserEntity activeUser;
    private final String VALID_CPF = "11144477735";

    @BeforeEach
    void setUp() {
        activeUser = UserEntity.builder()
                .username("john_doe")
                .cpf(VALID_CPF)
                .address("123 Street")
                .active(true)
                .build();

        entityManager.persist(activeUser);
        entityManager.flush();
    }

    @Test
    @DisplayName("Should return true when a user exists with the given CPF")
    void shouldReturnTrueWhenCpfExists() {
        Boolean exists = repository.existsByCpf(VALID_CPF);
        assertTrue(exists);
    }

    @Test
    @DisplayName("Should return true when another user exists with the same CPF excluding current ID")
    void shouldReturnTrueWhenCpfExistsAndIdNot() {
        UserEntity anotherUser = UserEntity.builder()
                .username("jane_doe")
                .cpf("22255588846")
                .address("456 Avenue")
                .active(true)
                .build();
        entityManager.persist(anotherUser);

        Boolean exists = repository.existsByCpfAndIdNot(VALID_CPF, anotherUser.getId());
        assertTrue(exists);
    }

    @Test
    @DisplayName("Should return false when checking CPF existence against its own ID")
    void shouldReturnFalseWhenCpfExistsButIsTheSameId() {
        Boolean exists = repository.existsByCpfAndIdNot(VALID_CPF, activeUser.getId());
        assertFalse(exists);
    }

    @Test
    @DisplayName("Should return a list of all active users")
    void shouldFindAllByActiveTrue() {
        UserEntity inactiveUser = UserEntity.builder()
                .username("ghost_user")
                .cpf("33366699957")
                .address("789 Blvd")
                .active(false)
                .build();
        entityManager.persist(inactiveUser);

        List<UserEntity> actives = repository.findAllByActiveTrue();

        assertAll(
                () -> assertEquals(1, actives.size()),
                () -> assertEquals("john_doe", actives.get(0).getUsername()),
                () -> assertTrue(actives.get(0).getActive())
        );
    }
}