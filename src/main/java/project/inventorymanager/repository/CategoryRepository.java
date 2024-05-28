package project.inventorymanager.repository;

import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.inventorymanager.model.product.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Set<Category> findAllByIdIn(Set<Long> categoryIds);

    boolean existsByName(String name);

    @Query(value = "SELECT COUNT(*) FROM categories WHERE name = :name",
            nativeQuery = true)
    Long existsByNameIncludingDeleted(@Param("name") String name);

    @Query(value = "SELECT * FROM categories WHERE name = :name AND is_deleted = TRUE",
            nativeQuery = true)
    Category findDeletedByName(@Param("name") String name);
}
