package project.inventorymanager.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import project.inventorymanager.dto.product.request.ProductRequestDto;
import project.inventorymanager.dto.product.response.ProductResponseDto;

public interface ProductService {
    ProductResponseDto save(ProductRequestDto requestDto, String email);

    ProductResponseDto getById(Long id, String email);

    List<ProductResponseDto> findAll(Pageable pageable, String email);

    void deleteById(Long id, String email);
}
