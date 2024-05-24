package project.inventorymanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.inventorymanager.dto.inventoryaction.request.InventoryActionRequestDto;
import project.inventorymanager.dto.inventoryaction.response.InventoryActionResponseDto;
import project.inventorymanager.service.InventoryActionService;

@Tag(name = "Inventories management", description = "Endpoints to managing inventories")
@RestController
@RequestMapping("/inventory-action")
@RequiredArgsConstructor
public class InventoryActionController {
    private final InventoryActionService inventoryActionService;

    @PostMapping
    @Operation(summary = "Create inventory action",
            description = "Save inventory action and update inventory")
    public InventoryActionResponseDto save(@RequestBody @Valid InventoryActionRequestDto requestDto,
                                           Authentication authentication) {
        return inventoryActionService.save(requestDto, authentication.getName());
    }

    //todo: create normal description for swagger
    @GetMapping
    @Operation(summary = "Find all inventory action",
            description = "Find all  action")
    public List<InventoryActionResponseDto> findAll(Pageable pageable,
                                                    Authentication authentication) {
        return inventoryActionService.findAll(pageable, authentication.getName());
    }
}
