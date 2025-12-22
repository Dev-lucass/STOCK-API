package com.example.estoque_api.repository;

import com.example.estoque_api.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsByCpf(String cpf);
    Boolean existsByCpfAndNot(String cpf, Long id);
}
