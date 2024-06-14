package project.inventorymanager.testutil.objects;

import org.springframework.stereotype.Component;
import project.inventorymanager.dto.category.request.CategoryRequestDto;
import project.inventorymanager.dto.category.response.CategoryResponseDto;
import project.inventorymanager.model.product.Category;

@Component
public class CategoryProvider {
    private static final String NAME = "example";

    public static Category getCategory(Long id) {
        Category category = new Category();
        category.setName(NAME + id);
        return category;
    }

    public static CategoryResponseDto getCategoryResponseDto(Long id) {
        CategoryResponseDto category = new CategoryResponseDto();
        category.setName(NAME + id);
        return category;
    }

    public static CategoryRequestDto getCategoryRequestDto(Long id) {
        CategoryRequestDto category = new CategoryRequestDto();
        category.setName(NAME + id);
        return category;
    }
}
