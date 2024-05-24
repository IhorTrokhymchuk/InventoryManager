package project.inventorymanager.service.impl;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.inventorymanager.dto.product.request.ProductRequestDto;
import project.inventorymanager.dto.product.response.ProductResponseDto;
import project.inventorymanager.mapper.ProductMapper;
import project.inventorymanager.model.product.Product;
import project.inventorymanager.model.user.User;
import project.inventorymanager.repositoryservice.ProductRepoService;
import project.inventorymanager.repositoryservice.UserRepoService;
import project.inventorymanager.service.ProductService;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final UserRepoService userRepoService;
    private final ProductRepoService productRepoService;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductResponseDto save(ProductRequestDto requestDto, String email) {
        productRepoService.isProductAlreadyExistWithUniqCode(requestDto.getUniqCode());
        User user = userRepoService.getByEmail(email);
        Product product = productMapper.toModelWithoutUser(requestDto);
        product.setUser(user);
        return productMapper.toResponseDto(productRepoService.save(product));
    }

    @Override
    public ProductResponseDto getById(Long id, String email) {
        return productMapper.toResponseDto(
                productRepoService.getByIdIfUserHavePermission(id, email));
    }

    @Override
    public List<ProductResponseDto> findAll(Pageable pageable, String email) {
        return productRepoService.findAllByUserEmail(pageable, email).stream()
                .map(productMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteById(Long id, String email) {
        productRepoService.deleteByIdIfUserHavePermission(id, email);
    }
}
