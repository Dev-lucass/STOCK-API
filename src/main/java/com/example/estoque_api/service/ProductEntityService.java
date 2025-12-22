package com.example.estoque_api.service;

import com.example.estoque_api.dto.request.ProductEntityDTO;
import com.example.estoque_api.dto.response.ProductEntityResponseDTO;
import com.example.estoque_api.exceptions.DuplicateResouceException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.mapper.ProductEntityMapper;
import com.example.estoque_api.model.ProductEntity;
import com.example.estoque_api.repository.ProductEntityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductEntityService {

    private final ProductEntityRepository repository;
    private final ProductEntityMapper mapper;

    public ProductEntityResponseDTO save(ProductEntityDTO dto) {
        validateDuplicateOnCreate(dto.name());
        var userEntityMapped = mapper.toEntityProduct(dto);
        repository.save(userEntityMapped);
        return mapper.toResponseEntityProduct(userEntityMapped);
    }

    public List<ProductEntityResponseDTO> findAllByActive() {
        return repository.findAllByActiveTrue()
                .stream()
                .map(mapper::toResponseEntityProduct)
                .toList();
    }

    public ProductEntityResponseDTO update(Long id, ProductEntityDTO dto) {
        var entity = findProductByIdOrElseThrow(id);
        validateDuplicateOnUpdate(dto.name(), id);
        mapper.updateEntity(entity, dto);
        repository.save(entity);
        return mapper.toResponseEntityProduct(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        var entity = findProductByIdOrElseThrow(id);
        entity.setActive(false);
    }

    private void validateDuplicateOnCreate(String name) {
        if (repository.existsByName(name))
            throw new DuplicateResouceException("Product already registered");
    }

    private void validateDuplicateOnUpdate(String name, Long id) {
        if (repository.existsByNameAndNot(name, id))
            throw new DuplicateResouceException("Product already registered");
    }

    private ProductEntity findProductByIdOrElseThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid product id"));
    }
}
