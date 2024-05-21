package project.inventorymanager.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import project.inventorymanager.dto.product.request.ProductRequestDto;
import project.inventorymanager.dto.product.response.ProductResponseDto;

public interface ProductService {
    ProductResponseDto save(ProductRequestDto requestDto);

    ProductResponseDto getById(Long id);

    List<ProductResponseDto> findAll(Pageable pageable);

    void deleteById(Long id);
}
