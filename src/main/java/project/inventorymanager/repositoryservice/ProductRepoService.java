package project.inventorymanager.repositoryservice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.inventorymanager.model.product.Product;

public interface ProductRepoService {
    Product getByIdIfUserHavePermission(Long id, String email);

    Page<Product> findAllByUserEmail(Pageable pageable, String email);

    //todo:
    void isProductAlreadyExistWithUniqCode(String uniqCode);

    void deleteByIdIfUserHavePermission(Long id, String email);

    Product save(Product product);
}
