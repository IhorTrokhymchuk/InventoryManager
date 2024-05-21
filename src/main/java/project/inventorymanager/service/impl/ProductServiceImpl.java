package project.inventorymanager.service.impl;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.inventorymanager.dto.product.request.ProductRequestDto;
import project.inventorymanager.dto.product.response.ProductResponseDto;
import project.inventorymanager.exception.repository.EntityAlreadyExistsException;
import project.inventorymanager.exception.repository.EntityNotFoundException;
import project.inventorymanager.mapper.ProductMapper;
import project.inventorymanager.model.product.Product;
import project.inventorymanager.repository.ProductRepository;
import project.inventorymanager.service.ProductService;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponseDto save(ProductRequestDto requestDto) {
        ifProductWithUniqCodePresent(requestDto.getUniqCode());
        Product product = productMapper.toModel(requestDto);
        return productMapper.toResponseDto(productRepository.save(product));
    }

    private void ifProductWithUniqCodePresent(String uniqCode) {
        if (productRepository.findProductByUniqCode(uniqCode).isPresent()) {
            throw new EntityAlreadyExistsException("Cant create entity with uniqCode: " + uniqCode);
        }
    }

    @Override
    public ProductResponseDto getById(Long id) {
        return productMapper.toResponseDto(getProductById(id));
    }

    private Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Cant find product with id:" + id));
    }

    @Override
    public List<ProductResponseDto> findAll(Pageable pageable) {
        return productRepository.findAll(pageable).stream()
                .map(productMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        getById(id);
        productRepository.deleteById(id);
    }
}
