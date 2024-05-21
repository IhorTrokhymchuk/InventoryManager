package project.inventorymanager.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import project.inventorymanager.model.product.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findProductByUniqCode(String uniqCode);
}
