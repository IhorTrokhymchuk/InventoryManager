package project.inventorymanager.repositoryservice.impl;

import jakarta.transaction.Transactional;
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
    public Product getById(Long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        "Cant find product with id: " + id));
    }

    @Override
    public Product getDeletedByUniqCode(String uniqCode) {
        return productRepository.findDeletedByUniqCode(uniqCode);
    }

    @Override
    public boolean ifExistDeletedWithUniqCode(String uniqCode) {
        Long exists = productRepository.existsByUniqCodeIncludingDeleted(uniqCode);
        return exists > 0;
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public void isExistWithUniqCode(String uniqCode) {
        if (productRepository.existsByUniqCode(uniqCode)) {
            throw new EntityAlreadyExistsException("Product with uniq code '"
                    + uniqCode + "' already exist");
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        }
        throw new EntityNotFoundException("Cant find product with id: " + id);

    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }
}
