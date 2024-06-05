package project.inventorymanager.repository.product;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.inventorymanager.model.product.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = {"categories"})
    Page<Product> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"categories"})
    Page<Product> findAll(Specification<Product> specification, Pageable pageable);

    @EntityGraph(attributePaths = {"categories"})
    Optional<Product> findById(Long id);

    boolean existsByUniqCode(String uniqCode);

    @Query(value = "SELECT COUNT(*) FROM products WHERE uniq_code = :uniqCode",
            nativeQuery = true)
    Long existsByUniqCodeIncludingDeleted(@Param("uniqCode") String uniqCode);

    @Query(value = "SELECT * FROM products WHERE uniq_code = :uniqCode AND is_deleted = TRUE",
            nativeQuery = true)
    Product findDeletedByUniqCode(@Param("uniqCode") String uniqCode);
}
