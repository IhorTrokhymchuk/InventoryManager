package project.inventorymanager.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static project.inventorymanager.testutil.objects.CategoryProvider.getCategory;
import static project.inventorymanager.testutil.objects.CategoryProvider.getCategoryRequestDto;
import static project.inventorymanager.testutil.objects.CategoryProvider.getCategoryResponseDto;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import project.inventorymanager.dto.category.request.CategoryRequestDto;
import project.inventorymanager.dto.category.response.CategoryResponseDto;
import project.inventorymanager.mapper.CategoryMapper;
import project.inventorymanager.model.product.Category;
import project.inventorymanager.repositoryservice.CategoryRepositoryService;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock
    private CategoryRepositoryService categoryRepositoryService;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Save valid category")
    void save_categoryWithUniqName_responseCategory() {
        Long id = 1L;
        CategoryRequestDto categoryRequestDto = getCategoryRequestDto(id);
        Category category = getCategory(id);
        CategoryResponseDto categoryResponseDto = getCategoryResponseDto(id);

        when(categoryRepositoryService.ifExistDeletedWithName(categoryRequestDto.getName()))
                .thenReturn(false);
        when(categoryMapper.toModel(categoryRequestDto)).thenReturn(category);
        when(categoryRepositoryService.save(category)).thenReturn(category);
        when(categoryMapper.toResponseDto(category)).thenReturn(categoryResponseDto);

        CategoryResponseDto result = categoryService.save(categoryRequestDto);

        assertNotNull(result);
        assertEquals(categoryResponseDto, result);

        verify(categoryRepositoryService, times(1)).isExistWithName(categoryRequestDto.getName());
        verify(categoryRepositoryService, times(1))
                .ifExistDeletedWithName(categoryRequestDto.getName());
        verify(categoryMapper, times(1)).toModel(categoryRequestDto);
        verify(categoryRepositoryService, times(1)).save(category);
        verify(categoryMapper, times(1)).toResponseDto(category);
        verifyNoMoreInteractions(categoryRepositoryService, categoryMapper);
    }

    @Test
    @DisplayName("Save category if exist deleted category")
    void save_categoryIfExistDeletedCategory_updatedResponseCategory() {
        Long id = 1L;
        CategoryRequestDto categoryRequestDto = getCategoryRequestDto(id);
        Category category = getCategory(id);
        CategoryResponseDto categoryResponseDto = getCategoryResponseDto(id);

        when(categoryRepositoryService.ifExistDeletedWithName(categoryRequestDto.getName()))
                .thenReturn(true);
        when(categoryRepositoryService.getDeletedByName(categoryRequestDto.getName()))
                .thenReturn(category);
        when(categoryRepositoryService.save(category)).thenReturn(category);
        when(categoryMapper.toResponseDto(category)).thenReturn(categoryResponseDto);

        CategoryResponseDto result = categoryService.save(categoryRequestDto);

        assertNotNull(result);
        assertEquals(categoryResponseDto, result);

        verify(categoryRepositoryService, times(1)).isExistWithName(categoryRequestDto.getName());
        verify(categoryRepositoryService, times(1))
                .ifExistDeletedWithName(categoryRequestDto.getName());
        verify(categoryRepositoryService, times(1)).getDeletedByName(categoryRequestDto.getName());
        verify(categoryMapper, times(1)).updateCategory(category, categoryRequestDto);
        verify(categoryRepositoryService, times(1)).save(category);
        verify(categoryMapper, times(1)).toResponseDto(category);
        verifyNoMoreInteractions(categoryRepositoryService, categoryMapper);
    }

    @Test
    @DisplayName("Get by id with exist data")
    void getById_existingCategoryId_ReturnsCategoryResponseDto() {
        Long id = 1L;
        CategoryResponseDto categoryResponseDto = getCategoryResponseDto(id);
        Category category = getCategory(id);

        when(categoryRepositoryService.getById(id)).thenReturn(category);
        when(categoryMapper.toResponseDto(category)).thenReturn(categoryResponseDto);

        CategoryResponseDto result = categoryService.getById(id);

        assertEquals(categoryResponseDto, result);
        verify(categoryRepositoryService, times(1)).getById(id);
        verify(categoryMapper, times(1)).toResponseDto(category);
        verifyNoMoreInteractions(categoryRepositoryService, categoryMapper);
    }

    @Test
    @DisplayName("Find all category with exist data")
    void findAll_existData_responseCategoryList() {
        Category category1 = getCategory(1L);
        Category category2 = getCategory(2L);

        List<Category> categories = new ArrayList<>();
        categories.add(category1);
        categories.add(category2);

        CategoryResponseDto categoryResponseDto1 = getCategoryResponseDto(1L);
        CategoryResponseDto categoryResponseDto2 = getCategoryResponseDto(2L);

        List<CategoryResponseDto> expectedDtos = new ArrayList<>();
        expectedDtos.add(categoryResponseDto1);
        expectedDtos.add(categoryResponseDto2);

        Pageable pageable = PageRequest.of(0, 10);

        Page<Category> accommodationPage = new PageImpl<>(categories);
        when(categoryRepositoryService.findAll(pageable)).thenReturn(accommodationPage);
        when(categoryMapper.toResponseDto(category1)).thenReturn(categoryResponseDto1);
        when(categoryMapper.toResponseDto(category2)).thenReturn(categoryResponseDto2);

        List<CategoryResponseDto> result = categoryService.findAll(pageable);
        assertEquals(expectedDtos, result);

        verify(categoryRepositoryService, times(1)).findAll(pageable);
        verify(categoryMapper, times(1)).toResponseDto(category1);
        verify(categoryMapper, times(1)).toResponseDto(category2);
        verifyNoMoreInteractions(categoryRepositoryService, categoryMapper);
    }

    @Test
    @DisplayName("Delete category by id with exist data")
    void deleteById_existingCategoryId_CategoryDeleted() {
        Long categoryId = 1L;

        categoryService.deleteById(categoryId);

        verify(categoryRepositoryService, times(1)).deleteById(categoryId);
        verifyNoMoreInteractions(categoryRepositoryService, categoryMapper);
    }
}
