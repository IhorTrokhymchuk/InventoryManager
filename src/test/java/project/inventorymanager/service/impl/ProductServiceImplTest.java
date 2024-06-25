package project.inventorymanager.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static project.inventorymanager.testutil.objects.CategoryProvider.getCategory;
import static project.inventorymanager.testutil.objects.ProductProvider.getProduct;
import static project.inventorymanager.testutil.objects.ProductProvider.getProductPatchDto;
import static project.inventorymanager.testutil.objects.ProductProvider.getProductRequestDto;
import static project.inventorymanager.testutil.objects.ProductProvider.getProductResponseDto;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;
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
import project.inventorymanager.repositoryservice.CategoryRepositoryService;
import project.inventorymanager.repositoryservice.ProductRepositoryService;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {
    private static final RoleType.RoleName EMPLOYEE = RoleType.RoleName.EMPLOYEE;
    private static final RoleType.RoleName USER = RoleType.RoleName.USER;
    @Mock
    private CategoryRepositoryService categoryRepositoryService;
    @Mock
    private ProductRepositoryService productRepositoryService;
    @Mock
    private ProductMapper productMapper;
    @Mock
    private SpecificationBuilder productSpecificationBuilder;
    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductResponseDto responseDto;
    private ProductRequestDto requestDto;
    private ProductPatchDto patchDto;
    private Page<Product> productPage;
    private Set<Category> categories;
    private Set<Long> categoryIds;

    @BeforeEach
    void setup() {
        Long id = 1L;
        product = getProduct(id);
        responseDto = getProductResponseDto(id);
        requestDto = getProductRequestDto(id);
        patchDto = getProductPatchDto(id);
        categoryIds = Set.of(1L, 2L);
        categories = Set.of(getCategory(1L), getCategory(2L));

        productPage = new PageImpl<>(List.of(product));
    }

    @Test
    @DisplayName("Save Product with valid data")
    void save_withValidData_product() {
        when(productRepositoryService.ifExistDeletedWithUniqCode(requestDto.getUniqCode()))
                .thenReturn(false);
        when(productMapper.toModelWithoutCategories(requestDto)).thenReturn(product);
        when(categoryRepositoryService.getAllByIdIn(categoryIds)).thenReturn(categories);
        when(productRepositoryService.save(product)).thenReturn(product);
        when(productMapper.toResponseDto(product)).thenReturn(responseDto);

        ProductResponseDto result = productService.save(requestDto);

        assertEquals(responseDto, result);
        verify(productRepositoryService, times(1))
                .isExistWithUniqCodeThrowException(requestDto.getUniqCode());
        verify(productRepositoryService, times(1))
                .ifExistDeletedWithUniqCode(requestDto.getUniqCode());
        verify(productMapper, times(1)).toModelWithoutCategories(requestDto);
        verify(categoryRepositoryService, times(1)).getAllByIdIn(categoryIds);
        verify(productRepositoryService, times(1)).save(product);
        verify(productMapper, times(1)).toResponseDto(product);
        verifyNoMoreInteractions(productRepositoryService, categoryRepositoryService,
                productMapper, productSpecificationBuilder);
    }

    @Test
    @DisplayName("Save Product with deleted uniqCode")
    void save_withValidExistDeletedUnicode_product() {
        when(productRepositoryService.ifExistDeletedWithUniqCode(requestDto.getUniqCode()))
                .thenReturn(true);
        when(productRepositoryService.getDeletedByUniqCode(requestDto.getUniqCode()))
                .thenReturn(product);
        when(categoryRepositoryService.getAllByIdIn(categoryIds)).thenReturn(categories);
        when(productRepositoryService.save(product)).thenReturn(product);
        when(productMapper.toResponseDto(product)).thenReturn(responseDto);

        ProductResponseDto result = productService.save(requestDto);

        assertEquals(responseDto, result);
        verify(productRepositoryService, times(1))
                .isExistWithUniqCodeThrowException(requestDto.getUniqCode());
        verify(productRepositoryService, times(1))
                .ifExistDeletedWithUniqCode(requestDto.getUniqCode());
        verify(productRepositoryService, times(1)).getDeletedByUniqCode(requestDto.getUniqCode());
        verify(productMapper, times(1)).setParametersWithoutCategories(product, requestDto);
        verify(categoryRepositoryService, times(1)).getAllByIdIn(categoryIds);
        verify(productRepositoryService, times(1)).save(product);
        verify(productMapper, times(1)).toResponseDto(product);
        verifyNoMoreInteractions(productRepositoryService, categoryRepositoryService,
                productMapper, productSpecificationBuilder);
    }

    @Test
    @DisplayName("Get Product by valid id with EMPLOYEE authority")
    void getById_withValidIdAndEmployeeAuthority_product() {
        GrantedAuthority authority = EMPLOYEE::name;
        Collection<GrantedAuthority> authorities = List.of(authority);
        when(productRepositoryService.getById(1L)).thenReturn(product);
        when(productMapper.toResponseDto(product)).thenReturn(responseDto);

        ProductResponseDto result = productService.getById(1L, authorities);

        assertEquals(responseDto, result);
        verify(productRepositoryService, times(1)).getById(1L);
        verify(productMapper, times(1)).toResponseDto(product);
        verifyNoMoreInteractions(productRepositoryService, categoryRepositoryService,
                productMapper, productSpecificationBuilder);
    }

    @Test
    @DisplayName("Get Product by valid id without EMPLOYEE authority")
    void getById_withValidIdWithoutEmployeeAuthority_product() {
        GrantedAuthority authority = USER::name;
        Collection<GrantedAuthority> authorities = List.of(authority);
        when(productRepositoryService.getById(1L)).thenReturn(product);
        when(productMapper.toResponseDtoWithoutWholesalePrice(product)).thenReturn(responseDto);

        ProductResponseDto result = productService.getById(1L, authorities);

        assertEquals(responseDto, result);
        verify(productRepositoryService, times(1)).getById(1L);
        verify(productMapper, times(1)).toResponseDtoWithoutWholesalePrice(product);
        verifyNoMoreInteractions(productRepositoryService, categoryRepositoryService,
                productMapper, productSpecificationBuilder);
    }

    @Test
    @DisplayName("Find all Products with valid pageable and EMPLOYEE authority")
    void findAll_withValidPageableAndEmployeeAuthority_products() {
        Pageable pageable = mock(Pageable.class);
        GrantedAuthority authority = EMPLOYEE::name;
        Collection<GrantedAuthority> authorities = List.of(authority);
        when(productRepositoryService.findAll(pageable)).thenReturn(productPage);
        when(productMapper.toResponseDto(product)).thenReturn(responseDto);

        List<ProductResponseDto> result = productService.findAll(pageable, authorities);

        assertEquals(List.of(responseDto), result);
        verify(productRepositoryService, times(1)).findAll(pageable);
        verify(productMapper, times(productPage.getNumberOfElements())).toResponseDto(product);
        verifyNoMoreInteractions(productRepositoryService, categoryRepositoryService,
                productMapper, productSpecificationBuilder);
    }

    @Test
    @DisplayName("Find all Products with valid pageable without EMPLOYEE authority")
    void findAll_withValidPageableWithoutEmployeeAuthority_products() {
        Pageable pageable = mock(Pageable.class);
        GrantedAuthority authority = USER::name;
        Collection<GrantedAuthority> authorities = List.of(authority);
        when(productRepositoryService.findAll(pageable)).thenReturn(productPage);
        when(productMapper.toResponseDtoWithoutWholesalePrice(product)).thenReturn(responseDto);

        List<ProductResponseDto> result = productService.findAll(pageable, authorities);

        assertEquals(List.of(responseDto), result);
        verify(productRepositoryService, times(1)).findAll(pageable);
        verify(productMapper, times(productPage.getNumberOfElements()))
                .toResponseDtoWithoutWholesalePrice(product);
        verifyNoMoreInteractions(productRepositoryService, categoryRepositoryService,
                productMapper, productSpecificationBuilder);
    }

    @Test
    @DisplayName("Patch Product by valid id")
    void patchById_withValidId_product() {
        Long id = 1L;
        when(productRepositoryService.getById(id)).thenReturn(product);
        when(categoryRepositoryService.getAllByIdIn(categoryIds)).thenReturn(categories);
        product.setRetailPrice(patchDto.getRetailPrice());
        product.setWholesalePrice(patchDto.getWholesalePrice());
        product.setName(patchDto.getName());
        when(productRepositoryService.save(product)).thenReturn(product);
        when(productMapper.toResponseDto(product)).thenReturn(responseDto);

        ProductResponseDto result = productService.patchById(id, patchDto);

        assertEquals(responseDto, result);
        verify(productRepositoryService, times(1)).getById(id);
        verify(productMapper, times(1)).setParametersWithoutCategories(product, patchDto);
        verify(categoryRepositoryService, times(1)).getAllByIdIn(categoryIds);
        verify(productRepositoryService, times(1)).save(product);
        verify(productMapper, times(1)).toResponseDto(product);
        verifyNoMoreInteractions(productRepositoryService, categoryRepositoryService,
                productMapper, productSpecificationBuilder);
    }

    @Test
    @DisplayName("Search Products with valid pageable and EMPLOYEE authority")
    void search_withValidPageableAndEmployeeAuthority_products() {
        Pageable pageable = mock(Pageable.class);
        GrantedAuthority authority = EMPLOYEE::name;
        Collection<GrantedAuthority> authorities = List.of(authority);
        ProductSearchDto searchDto = new ProductSearchDto();
        Specification<Product> specification = mock(Specification.class);
        when(productSpecificationBuilder.build(searchDto)).thenReturn(specification);
        when(productRepositoryService.findAll(pageable, specification)).thenReturn(productPage);
        when(productMapper.toResponseDto(product)).thenReturn(responseDto);

        List<ProductResponseDto> result = productService.search(pageable, authorities, searchDto);

        assertEquals(List.of(responseDto), result);
        verify(productSpecificationBuilder).build(searchDto);
        verify(productRepositoryService).findAll(pageable, specification);
        verify(productMapper, times(productPage.getNumberOfElements())).toResponseDto(product);
        verifyNoMoreInteractions(productRepositoryService, categoryRepositoryService,
                productMapper, productSpecificationBuilder);
    }

    @Test
    @DisplayName("Search Products with valid pageable and without EMPLOYEE authority")
    void search_withValidPageableAndUserAuthority_products() {

        ProductSearchDto searchDto = new ProductSearchDto();
        Specification<Product> specification = mock(Specification.class);
        searchDto.setWholesalePriceMin(null);
        searchDto.setWholesalePriceMax(null);
        when(productSpecificationBuilder.build(searchDto)).thenReturn(specification);
        Pageable pageable = mock(Pageable.class);
        when(productRepositoryService.findAll(pageable, specification)).thenReturn(productPage);
        when(productMapper.toResponseDtoWithoutWholesalePrice(product)).thenReturn(responseDto);

        GrantedAuthority authority = USER::name;
        Collection<GrantedAuthority> authorities = List.of(authority);
        List<ProductResponseDto> result = productService.search(pageable, authorities, searchDto);

        assertEquals(List.of(responseDto), result);
        verify(productSpecificationBuilder).build(searchDto);
        verify(productRepositoryService).findAll(pageable, specification);
        verify(productMapper, times(productPage.getNumberOfElements()))
                .toResponseDtoWithoutWholesalePrice(product);
        verifyNoMoreInteractions(productRepositoryService, categoryRepositoryService,
                productMapper, productSpecificationBuilder);
    }

    @Test
    @DisplayName("Search Products with invalid permissions")
    void search_withInvalidWholesalePriceMax_throwsException() {
        Pageable pageable = mock(Pageable.class);
        GrantedAuthority authority = USER::name;
        Collection<GrantedAuthority> authorities = List.of(authority);
        ProductSearchDto searchDto = new ProductSearchDto();
        searchDto.setWholesalePriceMax(BigDecimal.valueOf(100));

        assertThrows(UserDontHavePermissions.class, () -> productService
                .search(pageable, authorities, searchDto));
        verifyNoMoreInteractions(productRepositoryService, categoryRepositoryService,
                productMapper, productSpecificationBuilder);
    }

    @Test
    @DisplayName("Search Products with invalid permissions")
    void search_withInvalidWholesalePriceMin_throwsException() {
        Pageable pageable = mock(Pageable.class);
        GrantedAuthority authority = USER::name;
        Collection<GrantedAuthority> authorities = List.of(authority);
        ProductSearchDto searchDto = new ProductSearchDto();
        searchDto.setWholesalePriceMin(BigDecimal.valueOf(100));

        assertThrows(UserDontHavePermissions.class,
                () -> productService.search(pageable, authorities, searchDto));
        verifyNoMoreInteractions(productRepositoryService, categoryRepositoryService,
                productMapper, productSpecificationBuilder);
    }

    @Test
    @DisplayName("Delete Product by valid id")
    void deleteById_withValidId_product() {
        Long id = 1L;

        productService.deleteById(id);

        verify(productRepositoryService).deleteById(id);
        verifyNoMoreInteractions(productRepositoryService, categoryRepositoryService,
                productMapper, productSpecificationBuilder);
    }
}
