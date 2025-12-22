package com.example.estoque_api.service;

import com.example.estoque_api.exceptions.DuplicateResouceException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
import com.example.estoque_api.model.ProductEntity;
import com.example.estoque_api.repository.ProductEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductEntityService {

    private final ProductEntityRepository repository;

    public ProductEntity save(ProductEntity product) {
        return null;
    }

    public List<ProductEntity> findAll() {
        return null;
    }

    public ProductEntity update(Long id, ProductEntity productUpdated) {
        return null;
    }

    public void deleteById(Long id) {

    }

    private void validationProductEntityIsDuplicatedOnCreate(ProductEntity product) {
        if (repository.existsByName(product.getName()))
            throw new DuplicateResouceException("Product already registered");
    }

    private void validationProductEntityIsDuplicatedOnUpdate(ProductEntity product) {
        if (repository.existsByNameAndNot(
                product.getName(),
                product.getId())
        ) throw new DuplicateResouceException("Product already registered");
    }

    private ProductEntity findProductByIdOrElseThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid product id"));
    }
}
