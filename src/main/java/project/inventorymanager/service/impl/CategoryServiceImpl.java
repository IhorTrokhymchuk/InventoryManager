package project.inventorymanager.service.impl;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.inventorymanager.dto.category.request.CategoryRequestDto;
import project.inventorymanager.dto.category.response.CategoryResponseDto;
import project.inventorymanager.mapper.CategoryMapper;
import project.inventorymanager.model.product.Category;
import project.inventorymanager.repositoryservice.CategoryRepoService;
import project.inventorymanager.service.CategoryService;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepoService categoryRepoService;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryResponseDto save(CategoryRequestDto requestDto) {
        categoryRepoService.isExistWithName(requestDto.getName());
        Category category = getCategory(requestDto);
        return categoryMapper.toResponseDto(categoryRepoService.save(category));
    }

    private Category getCategory(CategoryRequestDto requestDto) {
        Category category;
        if (categoryRepoService.ifExistDeletedWithName(requestDto.getName())) {
            category = categoryRepoService.getDeletedByName(requestDto.getName());
            categoryMapper.updateCategory(category, requestDto);
        } else {
            category = categoryMapper.toModel(requestDto);
        }
        return category;
    }

    @Override
    public CategoryResponseDto getById(Long id) {
        return categoryMapper.toResponseDto(categoryRepoService.getById(id));
    }

    @Override
    public List<CategoryResponseDto> findAll(Pageable pageable) {
        return categoryRepoService.findAll(pageable).stream()
                .map(categoryMapper::toResponseDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        categoryRepoService.deleteById(id);
    }
}
