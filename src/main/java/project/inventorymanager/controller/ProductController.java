package project.inventorymanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import project.inventorymanager.dto.product.request.ProductRequestDto;
import project.inventorymanager.dto.product.response.ProductResponseDto;
import project.inventorymanager.service.ProductService;

@Tag(name = "Products management", description = "Endpoints to managing products")
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @Operation(summary = "Create product",
            description = "Save product to database")
    public ProductResponseDto save(@RequestBody @Valid ProductRequestDto requestDto) {
        return productService.save(requestDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by id",
            description = "Get existing product by id")
    public ProductResponseDto getById(@NotNull @PathVariable Long id) {
        return productService.getById(id);
    }

    @GetMapping
    @Operation(summary = "Get all products",
            description = "Get a page of all available products")
    public List<ProductResponseDto> findAll(Pageable pageable) {
        return productService.findAll(pageable);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete product by id",
            description = "Delete existing product by id")
    public void deleteById(@NotNull @PathVariable Long id) {
        productService.deleteById(id);
    }
}
