package project.inventorymanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
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
    public ProductResponseDto save(@RequestBody @Valid ProductRequestDto requestDto,
                                   Authentication authentication) {
        return productService.save(requestDto, authentication.getName());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by id",
            description = "Get existing user product by id")
    public ProductResponseDto getById(@NotNull @PathVariable Long id,
                                      Authentication authentication) {
        return productService.getById(id, authentication.getName());
    }

    @GetMapping
    @Operation(summary = "Get all products",
            description = "Get a page of all available user products")
    public List<ProductResponseDto> findAll(Pageable pageable, Authentication authentication) {
        return productService.findAll(pageable, authentication.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete product by id",
            description = "Delete existing user product by id")
    public void deleteById(@NotNull @PathVariable Long id, Authentication authentication) {
        productService.deleteById(id, authentication.getName());
    }
}
