package com.example.estoque_api.service;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProductEntityServiceTest {

    /*
    @Mock
    ProductEntityValidation validation;

    @Mock
    ProductEntityRepository repository;

    @InjectMocks
    ProductEntityService service;

    ProductEntity product;

    ProductEntity productUpdated;

    @BeforeEach
    void setUp() {
        product = new ProductEntity();
        product.setId(1L);
        product.setName("pen");
        product.setActive(true);

        productUpdated = new ProductEntity();
        productUpdated.setId(1L);
        productUpdated.setName("eraser");
        productUpdated.setActive(false);
    }

    @Test
    void should_save_product_successFully() {
        doNothing().when(validation).validationProductEntityIsDuplicatedOnCreate(product);
        when(repository.save(Mockito.any(ProductEntity.class))).thenReturn(product);

        ProductEntity result = service.save(product);

        assertNotNull(result);
        assertEquals(product, result);

        verify(validation).validationProductEntityIsDuplicatedOnCreate(product);
        verify(repository).save(product);
    }

    @Test
    void should_throw_exception_when_saving_duplicated_name() {
        doThrow(new DuplicateResouceException("PRODUCT already registered"))
                .when(validation)
                .validationProductEntityIsDuplicatedOnCreate(product);

        assertThrows(DuplicateResouceException.class, () -> service.save(product));

        verify(validation).validationProductEntityIsDuplicatedOnCreate(product);
        verify(repository, never()).save(any());
    }

    @Test
    void should_return_all_products() {
        when(repository.findAll()).thenReturn(List.of(product));

        List<ProductEntity> result = service.findAll();

        assertEquals(1, result.size());
        verify(repository).findAll();
    }


    @Test
    void should_update_product_successfully() {
        when(validation.validationProductEntityIdIsValid(1L)).thenReturn(product);
        doNothing().when(validation).validationProductEntityIsDuplicatedOnUpdate(productUpdated);

        ProductEntity result = service.update(1L, productUpdated);

        assertEquals("eraser", result.getName());
        assertEquals(false, result.getActive());

        verify(validation).validationProductEntityIdIsValid(1L);
        verify(validation).validationProductEntityIsDuplicatedOnUpdate(productUpdated);
    }


    @Test
    void should_throw_exception_when_updating_invalid_id() {
        when(validation.validationProductEntityIdIsValid(1L)).thenThrow(new ResourceNotFoundException("Invalid product ID"));
        assertThrows(ResourceNotFoundException.class, () -> service.update(1L, productUpdated));
    }

    @Test
    void should_delete_product_successfully() {
        when(validation.validationProductEntityIdIsValid(1L)).thenReturn(product);
        doNothing().when(repository).delete(product);
        service.deleteById(1L);
        verify(repository).delete(product);
    }

    @Test
    void should_throw_exception_when_deleting_invalid_id() {
        when(validation.validationProductEntityIdIsValid(1L)).thenThrow(new ResourceNotFoundException("Invalid product ID"));
        assertThrows(ResourceNotFoundException.class, () -> service.deleteById(1L));
        verify(repository, never()).delete(any());
    }

     */
}