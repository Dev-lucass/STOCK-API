package com.example.estoque_api.repository;

import com.example.estoque_api.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserEntityRepositoryTest {

    @Autowired
    private UserEntityRepository repository;

    private UserEntity activeUser;
    private UserEntity inactiveUser;

    @BeforeEach
    void setup() {
        activeUser = UserEntity.builder()
                .username("Lucas Silva")
                .cpf("11144477735")
                .address("Rua das Flores, 123")
                .active(true)
                .build();

        inactiveUser = UserEntity.builder()
                .username("Ana Souza")
                .cpf("98765432100")
                .address("Rua Central, 456")
                .active(false)
                .build();
    }

    @Test
    void should_save_user() {
        var savedUser = repository.save(activeUser);

        assertNotNull(savedUser.getId());
        assertEquals(activeUser.getCpf(), savedUser.getCpf());
    }

    @Test
    void should_find_all_active_users() {
        repository.save(activeUser);
        repository.save(inactiveUser);

        var result = repository.findAllByActiveTrue();

        assertEquals(1, result.size());
        assertTrue(result.getFirst().getActive());
    }

    @Test
    void should_return_true_when_cpf_exists() {
        repository.save(activeUser);
        Boolean exists = repository.existsByCpf("11144477735");
        assertTrue(exists);
    }

    @Test
    void should_return_false_when_cpf_does_not_exist() {
        Boolean exists = repository.existsByCpf("00000000000");
        assertFalse(exists);
    }

    @Test
    void should_return_true_when_cpf_exists_and_id_is_different() {
        var savedUser = repository.save(activeUser);
        Boolean exists = repository.existsByCpfAndIdNot("11144477735", savedUser.getId() + 1);
        assertTrue(exists);
    }

    @Test
    void should_return_false_when_cpf_exists_and_id_is_same() {
        var savedUser = repository.save(activeUser);
        Boolean exists = repository.existsByCpfAndIdNot("11144477735", savedUser.getId());
        assertFalse(exists);
    }

    @Test
    void should_update_user() {
        var savedUser = repository.save(activeUser);

        savedUser.setUsername("Lucas Atualizado");
        savedUser.setActive(false);

        var updatedUser = repository.save(savedUser);

        assertEquals(savedUser.getId(), updatedUser.getId());
        assertFalse(updatedUser.getActive());
        assertEquals("Lucas Atualizado", updatedUser.getUsername());
    }

    @Test
    void should_delete_user_by_id() {
        var savedUser = repository.save(activeUser);
        repository.deleteById(savedUser.getId());
        assertFalse(repository.findById(savedUser.getId()).isPresent());
    }
}
