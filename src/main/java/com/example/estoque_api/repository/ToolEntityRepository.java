package com.example.estoque_api.repository;

import com.example.estoque_api.model.ToolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

public interface ToolEntityRepository extends JpaRepository<ToolEntity, Long> , JpaSpecificationExecutor<ToolEntity> {
    Boolean existsByName(String name);
    Boolean existsByNameAndIdNot(String name, Long id);
    List<ToolEntity> findAllByActiveTrue();
    List<ToolEntity> findAllByActiveFalse();
}
