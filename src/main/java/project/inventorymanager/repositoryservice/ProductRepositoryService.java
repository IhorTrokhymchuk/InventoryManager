package project.inventorymanager.repositoryservice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import project.inventorymanager.model.product.Product;

public interface ProductRepositoryService {
    Product save(Product product);

    Product getById(Long id);

    Page<Product> findAll(Pageable pageable);

    Page<Product> findAll(Pageable pageable, Specification<Product> specification);

    void isExistWithUniqCodeThrowException(String uniqCode);

    boolean ifExistDeletedWithUniqCode(String uniqCode);

    Product getDeletedByUniqCode(String uniqCode);

    void deleteById(Long id);
}
