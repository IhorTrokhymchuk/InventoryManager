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
import project.inventorymanager.dto.product.request.ProductSearchDto;
import project.inventorymanager.dto.product.response.ProductResponseDto;
import project.inventorymanager.service.ProductService;

@Tag(name = "Products management", description = "Endpoints to managing products")
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "Create product",
            description = "Save product to database")
    public ProductResponseDto save(@RequestBody @Valid ProductRequestDto requestDto) {
        return productService.save(requestDto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Get product by id",
            description = "Get existing product by id")
    public ProductResponseDto getById(@NotNull @PathVariable Long id,
                                      Authentication authentication) {
        return productService.getById(id, authentication.getAuthorities());
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Get all products",
            description = "Get a page of all available products")
    public List<ProductResponseDto> findAll(Pageable pageable, Authentication authentication) {
        return productService.findAll(pageable, authentication.getAuthorities());
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Get products by parameters",
            description = "Get a page of all available products by parameters")
    public List<ProductResponseDto> search(Pageable pageable, Authentication authentication,
                                           @Valid ProductSearchDto requestDto) {
        return productService.search(pageable, authentication.getAuthorities(), requestDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete product by id",
            description = "Delete existing product by id")
    public void deleteById(@NotNull @PathVariable Long id) {
        productService.deleteById(id);
    }
}
