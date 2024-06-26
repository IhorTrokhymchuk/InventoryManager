package project.inventorymanager.repositoryservice.impl;

import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import project.inventorymanager.exception.repository.EntityAlreadyExistsException;
import project.inventorymanager.exception.repository.EntityNotFoundException;
import project.inventorymanager.model.product.Category;
import project.inventorymanager.repository.CategoryRepository;
import project.inventorymanager.repositoryservice.CategoryRepositoryService;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryServiceImpl implements CategoryRepositoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category getById(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Cant find category with id: " + id)
        );
    }

    @Override
    public void isExistWithName(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new EntityAlreadyExistsException(
                    "Category with name '" + name + "' is already exist");
        }
    }

    @Override
    public boolean ifExistDeletedWithName(String name) {
        Long exists = categoryRepository.existsByNameIncludingDeleted(name);
        return exists > 0;
    }

    @Override
    public Category getDeletedByName(String name) {
        Optional<Category> optionalCategory = categoryRepository.findDeletedByName(name);
        if (optionalCategory.isPresent()) {
            return optionalCategory.get();
        }
        throw new EntityNotFoundException("Cant find deleted category with name: " + name);
    }

    @Override
    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Cant find category with id: " + id);
        }
    }

    @Override
    public Set<Category> getAllByIdIn(Set<Long> categoryIds) {
        Set<Category> categories = categoryRepository.findAllByIdIn(categoryIds);
        if (categories.isEmpty()) {
            throw new EntityNotFoundException("Cant find any category with ids: " + categoryIds);
        }
        return categories;
    }
}
