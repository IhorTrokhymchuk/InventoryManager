package project.inventorymanager.service.impl;

import jakarta.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import project.inventorymanager.dto.product.request.ProductPatchDto;
import project.inventorymanager.dto.product.request.ProductRequestDto;
import project.inventorymanager.dto.product.request.ProductSearchDto;
import project.inventorymanager.dto.product.response.ProductResponseDto;
import project.inventorymanager.exception.user.UserDontHavePermissions;
import project.inventorymanager.mapper.ProductMapper;
import project.inventorymanager.model.product.Category;
import project.inventorymanager.model.product.Product;
import project.inventorymanager.model.user.RoleType;
import project.inventorymanager.repository.specefication.SpecificationBuilder;
import project.inventorymanager.repositoryservice.CategoryRepoService;
import project.inventorymanager.repositoryservice.ProductRepoService;
import project.inventorymanager.service.ProductService;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final CategoryRepoService categoryRepoService;
    private final ProductRepoService productRepoService;
    private final ProductMapper productMapper;
    private final SpecificationBuilder productSpecificationBuilder;

    @Override
    @Transactional
    public ProductResponseDto save(ProductRequestDto requestDto) {
        productRepoService.isExistWithUniqCode(requestDto.getUniqCode());
        Product product = getProductWithoutCategories(requestDto);
        setCategories(requestDto.getCategoryIds(), product);
        return productMapper.toResponseDto(
                productRepoService.save(product));
    }

    private Product getProductWithoutCategories(ProductRequestDto requestDto) {
        Product product;
        if (productRepoService.ifExistDeletedWithUniqCode(requestDto.getUniqCode())) {
            product = productRepoService.getDeletedByUniqCode(requestDto.getUniqCode());
            productMapper.setParametersWithoutCategories(product, requestDto);
            product.setDeleted(false);
        } else {
            product = productMapper.toModelWithoutCategories(requestDto);
        }
        return product;
    }

    @Override
    public ProductResponseDto getById(Long id, Collection<? extends GrantedAuthority> authorities) {
        Product product = productRepoService.getById(id);
        if (authoritiesContainsNameEmployee(authorities)) {
            return productMapper.toResponseDto(product);
        }
        return productMapper.toResponseDtoWithoutWholesalePrice(product);
    }

    private boolean authoritiesContainsNameEmployee(
            Collection<? extends GrantedAuthority> authorities) {
        List<String> authorityNames = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return authorityNames.contains(RoleType.RoleName.EMPLOYEE.name());
    }

    @Override
    public List<ProductResponseDto> findAll(Pageable pageable,
                                            Collection<? extends GrantedAuthority> authorities) {
        Page<Product> products = productRepoService.findAll(pageable);
        if (authoritiesContainsNameEmployee(authorities)) {
            return getProductResponseDtos(products);
        }
        return getResponseDtosWithoutWholesalePrice(products);
    }

    private List<ProductResponseDto> getProductResponseDtos(Page<Product> products) {
        return products.stream()
                .map(productMapper::toResponseDto)
                .toList();
    }

    private List<ProductResponseDto> getResponseDtosWithoutWholesalePrice(Page<Product> products) {
        return products.stream()
                .map(productMapper::toResponseDtoWithoutWholesalePrice)
                .toList();
    }

    @Override
    public ProductResponseDto patchById(Long id, ProductPatchDto requestDto) {
        Product product = productRepoService.getById(id);
        productMapper.setParametersWithoutCategories(product, requestDto);
        setCategories(requestDto.getCategoryIds(), product);
        return productMapper.toResponseDto(productRepoService.save(product));
    }

    private void setCategories(Set<Long> requestDto, Product product) {
        Set<Category> categories = categoryRepoService.getAllByIdIn(requestDto);
        product.setCategories(categories);
    }

    @Override
    public List<ProductResponseDto> search(
            Pageable pageable,
            Collection<? extends GrantedAuthority> authorities,
            ProductSearchDto requestDto) {
        checkUserPermission(authorities, requestDto);
        Specification<Product> specification = productSpecificationBuilder.build(requestDto);
        Page<Product> products =
                productRepoService.findAll(pageable, specification);
        if (authoritiesContainsNameEmployee(authorities)) {
            return getProductResponseDtos(products);
        }
        return getResponseDtosWithoutWholesalePrice(products);
    }

    private void checkUserPermission(
            Collection<? extends GrantedAuthority> authorities, ProductSearchDto requestDto) {
        if (!authoritiesContainsNameEmployee(authorities)) {
            if (requestDto.getWholesalePriceMin() != null
                    || requestDto.getWholesalePriceMax() != null) {
                throw new UserDontHavePermissions("User with roles: " + authorities
                        + " cant search by WholesalePriceMax and WholesalePriceMin");
            }
        }
    }

    @Override
    public void deleteById(Long id) {
        productRepoService.deleteById(id);
    }
}
