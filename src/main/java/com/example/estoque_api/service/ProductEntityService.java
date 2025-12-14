package com.example.estoque_api.service;

import com.example.estoque_api.model.ProductEntity;
import com.example.estoque_api.repository.ProductEntityRepository;
import com.example.estoque_api.validation.ProductEntityValidation;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductEntityService {

    private final ProductEntityRepository repository;
    private final ProductEntityValidation validation;

    public ProductEntityService(ProductEntityRepository repository, ProductEntityValidation validation) {
        this.repository = repository;
        this.validation = validation;
    }

    @Transactional
    public ProductEntity save(ProductEntity product) {
        validation.validationProductEntityIsDuplicatedOnCreate(product);
        return repository.save(product);
    }

    public List<ProductEntity> findAll() {
        return repository.findAll();
    }

    @Transactional
    public ProductEntity update(Long id, ProductEntity productUpdated) {

        ProductEntity productEntityFounded = validation.validationProductEntityIdIsValid(id);
        validation.validationProductEntityIsDuplicatedOnUpdate(productUpdated);

        productEntityFounded.setName(productUpdated.getName());
        productEntityFounded.setActive(productUpdated.getActive());

        return productEntityFounded;
    }

    @Transactional
    public void deleteById(Long id) {
        ProductEntity productEntityFounded = validation.validationProductEntityIdIsValid(id);
        repository.delete(productEntityFounded);
    }

}
