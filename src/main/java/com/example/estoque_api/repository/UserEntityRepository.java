package com.example.estoque_api.repository;

import com.example.estoque_api.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsByCpf(String cpf);
    Boolean existsByCpfAndIdNot(String cpf, Long id);
    List<UserEntity> findAllByActiveTrue();
}
