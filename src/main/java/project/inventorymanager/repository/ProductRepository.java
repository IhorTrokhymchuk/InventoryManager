package project.inventorymanager.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.inventorymanager.model.product.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    //todo:fixed delete with uniqCode
    Optional<Product> findProductByUniqCode(String uniqCode);

    Optional<Product> findProductByIdAndUserEmail(Long id, String email);

    Page<Product> findAllByUserEmail(Pageable pageable, String email);

    Integer deleteByIdAndUserEmail(Long id, String email);
}
