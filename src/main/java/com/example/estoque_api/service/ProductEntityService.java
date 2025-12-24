package com.example.estoque_api.service;

import com.example.estoque_api.dto.request.ProductEntityDTO;
import com.example.estoque_api.dto.response.entity.ProductEntityResponseDTO;
import com.example.estoque_api.exceptions.DuplicateResouceException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.mapper.ProductEntityMapper;
import com.example.estoque_api.model.ProductEntity;
import com.example.estoque_api.repository.ProductEntityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.estoque_api.repository.specs.ProductEntitySpec.likeName;

@Service
@RequiredArgsConstructor
public class ProductEntityService {

    private final ProductEntityRepository repository;
    private final ProductEntityMapper mapper;

    public ProductEntityResponseDTO save(ProductEntityDTO dto) {
        validateDuplicateOnCreate(dto.name());
        var userEntityMapped = mapper.toEntityProduct(dto);
        var productSaved = repository.save(userEntityMapped);
        return mapper.toResponseEntityProduct(productSaved);
    }

    public List<ProductEntityResponseDTO> findAllIsActive() {
        return repository.findAllByActiveTrue()
                .stream()
                .map(mapper::toResponseEntityProduct)
                .toList();
    }

    public List<ProductEntityResponseDTO> findAllIsNotActive() {
        return repository.findAllByActiveFalse()
                .stream()
                .map(mapper::toResponseEntityProduct)
                .toList();
    }

    public ProductEntityResponseDTO update(Long id, ProductEntityDTO dto) {
        var entity = findProductByIdOrElseThrow(id);
        validateDuplicateOnUpdate(dto.name(), id);
        mapper.updateEntity(entity, dto);
        var productUpdated = repository.save(entity);
        return mapper.toResponseEntityProduct(productUpdated);
    }

    @Transactional
    public void disableById(Long id) {
        var entity = findProductByIdOrElseThrow(id);
        entity.setActive(false);
    }

    public Page<ProductEntity> filterByNamePageable(String name, int pageNumber, int pageSize) {

        var specification = buildSpecification(name);

        Pageable pageable = PageRequest.of(
                pageNumber,
                pageSize,
                Sort.by("name").ascending());

        return repository.findAll(specification, pageable);
    }

    private Specification<ProductEntity> buildSpecification(String name) {
        Specification<ProductEntity> specification = null;

        if (name != null) {
            specification = likeName(name);
        }

        return specification;
    }

    private void validateDuplicateOnCreate(String name) {
        if (repository.existsByName(name))
            throw new DuplicateResouceException("Product already registered");
    }

    private void validateDuplicateOnUpdate(String name, Long id) {
        if (repository.existsByNameAndIdNot(name, id))
            throw new DuplicateResouceException("Product already registered");
    }

    public ProductEntity findProductByIdOrElseThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid product id"));
    }
}
