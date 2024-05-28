package project.inventorymanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import project.inventorymanager.dto.category.request.CategoryRequestDto;
import project.inventorymanager.dto.category.response.CategoryResponseDto;
import project.inventorymanager.service.CategoryService;

@Tag(name = "Categories management", description = "Endpoints to managing categories")
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Create category",
            description = "Save category to database")
    public CategoryResponseDto save(@RequestBody @Valid CategoryRequestDto requestDto) {
        return categoryService.save(requestDto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Get category by id",
            description = "Get existing category by id")
    public CategoryResponseDto getById(@NotNull @PathVariable Long id) {
        return categoryService.getById(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Get all categories",
            description = "Get a page of all available categories")
    public List<CategoryResponseDto> findAll(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete category by id",
            description = "Delete existing category by id")
    public void deleteById(@NotNull @PathVariable Long id) {
        categoryService.deleteById(id);
    }
}
