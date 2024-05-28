package project.inventorymanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import project.inventorymanager.config.MapperConfig;
import project.inventorymanager.dto.category.request.CategoryRequestDto;
import project.inventorymanager.dto.category.response.CategoryResponseDto;
import project.inventorymanager.model.product.Category;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryResponseDto toResponseDto(Category category);

    Category toModel(CategoryRequestDto requestDto);

    void updateCategory(@MappingTarget Category category, CategoryRequestDto requestDto);
}
