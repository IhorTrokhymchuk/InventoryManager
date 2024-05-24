package project.inventorymanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.inventorymanager.dto.inventory.response.InventoryResponseDto;
import project.inventorymanager.service.InventoryService;

@Tag(name = "Inventories management", description = "Endpoints to managing inventories")
@RestController
@RequestMapping("/inventories")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping
    @Operation(summary = "Get all inventories",
            description = "Get a page of all available user inventories")
    public List<InventoryResponseDto> findAll(Pageable pageable, Authentication authentication) {
        return inventoryService.findAll(pageable, authentication.getName());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get inventory by id",
            description = "Get a available inventory if user have permission")
    public InventoryResponseDto getById(@PathVariable Long id, Authentication authentication) {
        return inventoryService.getById(id, authentication.getName());
    }
}
