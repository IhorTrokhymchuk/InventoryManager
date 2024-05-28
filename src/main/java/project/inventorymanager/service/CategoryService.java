package project.inventorymanager.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import project.inventorymanager.dto.category.request.CategoryRequestDto;
import project.inventorymanager.dto.category.response.CategoryResponseDto;

public interface CategoryService {
    CategoryResponseDto save(CategoryRequestDto requestDto);

    CategoryResponseDto getById(Long id);

    List<CategoryResponseDto> findAll(Pageable pageable);

    void deleteById(Long id);
}
