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
import project.inventorymanager.dto.warehouse.request.WarehouseRequestDto;
import project.inventorymanager.dto.warehouse.response.WarehouseResponseDto;
import project.inventorymanager.service.WarehouseService;

@Tag(name = "Warehouse management", description = "Endpoints to managing warehouses")
@RestController
@RequestMapping(value = "/warehouses")
@RequiredArgsConstructor
public class WarehouseController {
    private final WarehouseService warehouseService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Create warehouse",
            description = "Save warehouse to database")
    @PostMapping
    public WarehouseResponseDto save(@RequestBody @Valid WarehouseRequestDto requestDto) {
        return warehouseService.save(requestDto);
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @Operation(summary = "Get warehouses by id",
            description = "Get warehouses by id or throw exception")
    @GetMapping("/{id}")
    public WarehouseResponseDto getById(@PathVariable @NotNull Long id) {
        return warehouseService.getById(id);
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @Operation(summary = "Get all warehouses",
            description = "Get a list of all available warehouses")
    @GetMapping
    public List<WarehouseResponseDto> findAll(Pageable pageable) {
        return warehouseService.findAll(pageable);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete product by id",
            description = "Delete existing user product by id")
    public void deleteById(@NotNull @PathVariable Long id) {
        warehouseService.deleteById(id);
    }
}
