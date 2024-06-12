package project.inventorymanager.repositoryservice.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import project.inventorymanager.exception.repository.EntityAlreadyExistsException;
import project.inventorymanager.exception.repository.EntityNotFoundException;
import project.inventorymanager.model.product.Product;
import project.inventorymanager.repository.product.ProductRepository;

@ExtendWith(SpringExtension.class)
public class ProductRepositoryServiceImplTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductRepositoryServiceImpl productRepositoryService;

    @Test
    @DisplayName("Test get product by id successfully")
    public void testGetById() {
        Long id = 1L;
        Product product = new Product();
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        Product foundProduct = productRepositoryService.getById(id);

        assertNotNull(foundProduct);
        verify(productRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Test get product by id throws EntityNotFoundException")
    public void testGetByIdNotFound() {
        Long id = 1L;
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                productRepositoryService.getById(id));

        assertEquals("Cant find product with id: " + id, exception.getMessage());
        verify(productRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Test get deleted product by uniq code successfully")
    public void testGetDeletedByUniqCode() {
        String uniqCode = "ABC123";
        Product product = new Product();
        when(productRepository.findDeletedByUniqCode(uniqCode)).thenReturn(Optional.of(product));

        Product foundProduct = productRepositoryService.getDeletedByUniqCode(uniqCode);

        assertNotNull(foundProduct);
        verify(productRepository, times(1)).findDeletedByUniqCode(uniqCode);
    }

    @Test
    @DisplayName("Test get deleted product by uniq code throws EntityNotFoundException")
    public void testGetDeletedByUniqCodeNotFound() {
        String uniqCode = "ABC123";
        when(productRepository.findDeletedByUniqCode(uniqCode)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                productRepositoryService.getDeletedByUniqCode(uniqCode));

        assertEquals("Cant find entity where uniq code: " + uniqCode + " and is deleted: true",
                exception.getMessage());
        verify(productRepository, times(1)).findDeletedByUniqCode(uniqCode);
    }

    @Test
    @DisplayName("Test if deleted product exists by uniq code")
    public void testIfExistDeletedWithUniqCode() {
        String uniqCode = "ABC123";
        when(productRepository.existsByUniqCodeIncludingDeleted(uniqCode)).thenReturn(1L);

        boolean exists = productRepositoryService.ifExistDeletedWithUniqCode(uniqCode);

        assertTrue(exists);
        verify(productRepository, times(1)).existsByUniqCodeIncludingDeleted(uniqCode);
    }

    @Test
    @DisplayName("Test find all products")
    public void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of(new Product()));
        when(productRepository.findAll(pageable)).thenReturn(page);

        Page<Product> result = productRepositoryService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(productRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Test find all products with specification")
    public void testFindAllWithSpecification() {
        Pageable pageable = PageRequest.of(0, 10);
        Specification<Product> specification = mock(Specification.class);
        Page<Product> page = new PageImpl<>(List.of(new Product()));
        when(productRepository.findAll(specification, pageable)).thenReturn(page);

        Page<Product> result = productRepositoryService.findAll(pageable, specification);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(productRepository, times(1)).findAll(specification, pageable);
    }

    @Test
    @DisplayName("Test check if product exists by uniq code throws EntityAlreadyExistsException")
    public void testIsExistWithUniqCode() {
        String uniqCode = "ABC123";
        when(productRepository.existsByUniqCode(uniqCode)).thenReturn(true);

        EntityAlreadyExistsException exception = assertThrows(
                EntityAlreadyExistsException.class, () ->
                        productRepositoryService.isExistWithUniqCode(uniqCode));

        assertEquals("Product with uniq code '" + uniqCode
                + "' already exist", exception.getMessage());
        verify(productRepository, times(1)).existsByUniqCode(uniqCode);
    }

    @Test
    @DisplayName("Test delete product by id successfully")
    public void testDeleteById() {
        Long id = 1L;
        when(productRepository.existsById(id)).thenReturn(true);

        productRepositoryService.deleteById(id);

        verify(productRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Test delete product by id throws EntityNotFoundException")
    public void testDeleteByIdNotFound() {
        Long id = 1L;
        when(productRepository.existsById(id)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                productRepositoryService.deleteById(id));

        assertEquals("Cant find product with id: " + id, exception.getMessage());
        verify(productRepository, times(1)).existsById(id);
    }

    @Test
    @DisplayName("Test save product successfully")
    public void testSaveProduct() {
        Product product = new Product();
        when(productRepository.save(product)).thenReturn(product);

        Product savedProduct = productRepositoryService.save(product);

        assertNotNull(savedProduct);
        verify(productRepository, times(1)).save(product);
    }
}
