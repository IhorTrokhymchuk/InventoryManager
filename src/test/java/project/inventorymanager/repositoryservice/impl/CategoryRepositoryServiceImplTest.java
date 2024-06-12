package project.inventorymanager.repositoryservice.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import project.inventorymanager.exception.repository.EntityAlreadyExistsException;
import project.inventorymanager.exception.repository.EntityNotFoundException;
import project.inventorymanager.model.product.Category;
import project.inventorymanager.repository.CategoryRepository;

@ExtendWith(SpringExtension.class)
public class CategoryRepositoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryRepositoryServiceImpl categoryRepositoryService;

    @Test
    @DisplayName("Test save category successfully")
    public void save_saveCategory_savedCategory() {
        Category category = new Category();
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category savedCategory = categoryRepositoryService.save(category);

        assertNotNull(savedCategory);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    @DisplayName("Test get category by id successfully")
    public void getById_getByIdWithExistData_category() {
        Long id = 1L;
        Category category = new Category();
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        Category foundCategory = categoryRepositoryService.getById(id);

        assertNotNull(foundCategory);
        verify(categoryRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Test get category by id throws EntityNotFoundException")
    public void getById_getByIdWithNonExistData_exception() {
        Long id = 1L;
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class, () -> categoryRepositoryService.getById(id));

        assertEquals("Cant find category with id: " + id, exception.getMessage());
        verify(categoryRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Test isExistWithName throws EntityAlreadyExistsException")
    public void isExistWithName_withExistData_exception() {
        String name = "Electronics";
        when(categoryRepository.existsByName(name)).thenReturn(true);

        EntityAlreadyExistsException exception = assertThrows(
                EntityAlreadyExistsException.class, () ->
                        categoryRepositoryService.isExistWithName(name));

        assertEquals("Category with name '" + name + "' is already exist", exception.getMessage());
        verify(categoryRepository, times(1)).existsByName(name);
    }

    @Test
    @DisplayName("Test ifExistDeletedWithName returns true")
    public void ifExistDeletedWithName_WithExistData_true() {
        String name = "Electronics";
        when(categoryRepository.existsByNameIncludingDeleted(name)).thenReturn(1L);

        boolean exists = categoryRepositoryService.ifExistDeletedWithName(name);

        assertTrue(exists);
        verify(categoryRepository, times(1)).existsByNameIncludingDeleted(name);
    }

    @Test
    @DisplayName("Test getDeletedByName successfully")
    public void getDeletedByName_WithExistData_deletedCategory() {
        String name = "Electronics";
        Category category = new Category();
        when(categoryRepository.findDeletedByName(name)).thenReturn(Optional.of(category));

        Category foundCategory = categoryRepositoryService.getDeletedByName(name);

        assertNotNull(foundCategory);
        verify(categoryRepository, times(1)).findDeletedByName(name);
    }

    @Test
    @DisplayName("Test getDeletedByName throws EntityNotFoundException")
    public void getDeletedByName_WithNonExistData_exception() {
        String name = "Electronics";
        when(categoryRepository.findDeletedByName(name)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class, () ->
                        categoryRepositoryService.getDeletedByName(name));

        assertEquals("Cant find deleted category with name: " + name, exception.getMessage());
        verify(categoryRepository, times(1)).findDeletedByName(name);
    }

    @Test
    @DisplayName("Test findAll categories")
    public void findAll_findAllWithExistData_categoryList() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> page = new PageImpl<>(List.of(new Category()));
        when(categoryRepository.findAll(pageable)).thenReturn(page);

        Page<Category> result = categoryRepositoryService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(categoryRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Test deleteById successfully")
    public void deleteById_WithExistData_delete() {
        Long id = 1L;
        when(categoryRepository.existsById(id)).thenReturn(true);

        categoryRepositoryService.deleteById(id);

        verify(categoryRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Test deleteById throws EntityNotFoundException")
    public void deleteById_WithNonExistData_exception() {
        Long id = 1L;

        when(categoryRepository.existsById(id)).thenReturn(false);
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class, () -> categoryRepositoryService.deleteById(id));

        assertEquals("Cant find category with id: " + id, exception.getMessage());
        verify(categoryRepository, times(1)).existsById(id);
        verify(categoryRepository, times(0)).deleteById(id);
    }

    @Test
    @DisplayName("Test getAllByIdIn successfully")
    public void getAllByIdIn_withExistData_categoryList() {
        Set<Long> ids = Set.of(1L, 2L);
        Set<Category> categories = Set.of(new Category());
        when(categoryRepository.findAllByIdIn(ids)).thenReturn(categories);

        Set<Category> result = categoryRepositoryService.getAllByIdIn(ids);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(categoryRepository, times(1)).findAllByIdIn(ids);
    }

    @Test
    @DisplayName("Test getAllByIdIn throws EntityNotFoundException")
    public void getAllByIdIn_withNonExistData_exception() {
        Set<Long> ids = Set.of(1L, 2L);
        when(categoryRepository.findAllByIdIn(ids)).thenReturn(Set.of());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class, () -> categoryRepositoryService.getAllByIdIn(ids));

        assertEquals("Cant find any category with ids: " + ids, exception.getMessage());
        verify(categoryRepository, times(1)).findAllByIdIn(ids);
    }
}
