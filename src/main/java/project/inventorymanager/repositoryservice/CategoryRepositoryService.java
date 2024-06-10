package project.inventorymanager.repositoryservice;

import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.inventorymanager.model.product.Category;

public interface CategoryRepositoryService {
    Category save(Category category);

    Category getById(Long id);

    Page<Category> findAll(Pageable pageable);

    Set<Category> getAllByIdIn(Set<Long> categoryIds);

    void isExistWithName(String name);

    boolean ifExistDeletedWithName(String name);

    Category getDeletedByName(String name);

    void deleteById(Long id);
}
