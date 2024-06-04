package project.inventorymanager.service;

import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import project.inventorymanager.dto.product.request.ProductRequestDto;
import project.inventorymanager.dto.product.request.ProductSearchDto;
import project.inventorymanager.dto.product.response.ProductResponseDto;

public interface ProductService {
    ProductResponseDto save(ProductRequestDto requestDto);

    ProductResponseDto getById(Long id,
                               Collection<? extends GrantedAuthority> authorities);

    List<ProductResponseDto> findAll(Pageable pageable,
                                     Collection<? extends GrantedAuthority> authorities);

    void deleteById(Long id);

    List<ProductResponseDto> search(Pageable pageable,
                                    Collection<? extends GrantedAuthority> authorities,
                                    ProductSearchDto requestDto);
}
