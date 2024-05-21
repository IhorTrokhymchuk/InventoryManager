package project.inventorymanager.repositoryservice.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.inventorymanager.exception.repository.EntityAlreadyExistsException;
import project.inventorymanager.exception.repository.EntityNotFoundException;
import project.inventorymanager.model.product.Product;
import project.inventorymanager.repository.ProductRepository;
import project.inventorymanager.repositoryservice.ProductRepoService;

@Service
@RequiredArgsConstructor
public class ProductRepoServiceImpl implements ProductRepoService {
    private final ProductRepository productRepository;

    @Override
    public Product getByIdIfUserHavePermission(Long id, String email) {
        return productRepository.findProductByIdAndUserEmail(id, email).orElseThrow(
                () -> new EntityNotFoundException(
                        "Cant find product with user email: " + email + " and id: " + id));
    }

    @Override
    //todo:fixed delete with uniqCode
    public void isProductAlreadyExistWithUniqCode(String uniqCode) {
        Optional<Product> optionalProduct = productRepository.findProductByUniqCode(uniqCode);
        if (optionalProduct.isPresent()) {
            throw new EntityAlreadyExistsException(
                    "Product with uniqCode: " + uniqCode + " is already exist");
        }
    }

    @Override
    public Page<Product> findAllByUserEmail(Pageable pageable, String email) {
        return productRepository.findAllByUserEmail(pageable, email);
    }

    @Override
    public void deleteByIdIfUserHavePermission(Long id, String email) {
        Integer result = productRepository.deleteByIdAndUserEmail(id, email);
        if (result == 0) {
            throw new EntityNotFoundException(
                    "Cant delete product with user email: " + email + " adn id: " + id);
        }
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }
}
