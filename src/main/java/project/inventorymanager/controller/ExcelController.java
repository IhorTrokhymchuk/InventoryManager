package project.inventorymanager.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.inventorymanager.exceldata.service.InventoryActionTableCreator;

@Tag(name = "Inventories management", description = "Endpoints to managing inventories")
@RestController
@RequestMapping("/excel")
@RequiredArgsConstructor
public class ExcelController {
    private final InventoryActionTableCreator inventoryActionTableCreator;

    @PostMapping("/user-info")
    public void getExcelUserInfo(Authentication authentication) {
        inventoryActionTableCreator.createTable(authentication.getName());
    }

}
