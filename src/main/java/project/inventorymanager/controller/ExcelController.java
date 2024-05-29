package project.inventorymanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.inventorymanager.dto.excel.DatesDto;
import project.inventorymanager.service.ExcelCreatorService;

@Tag(name = "Inventories management", description = "Endpoints to managing inventories")
@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class ExcelController {
    private final ExcelCreatorService excelCreatorService;

    @PostMapping("/inventory-actions")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Create statistic file", description = "Create statistic by date")
    public String getExcelUserInfo(
            @RequestBody DatesDto requestDto, Authentication authentication) {
        return excelCreatorService.createInventoryActionStatistic(
                requestDto,authentication.getName());
    }
}
