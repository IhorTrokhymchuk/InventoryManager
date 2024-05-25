package project.inventorymanager.exceldata.service.impl;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.inventorymanager.dto.inventoryaction.excel.InventoryActionExcelDto;
import project.inventorymanager.exceldata.service.InventoryActionTableCreator;
import project.inventorymanager.exceldata.tablescreator.InventoryActonTable;
import project.inventorymanager.mapper.InventoryActionMapper;
import project.inventorymanager.model.inventoryaction.InventoryAction;
import project.inventorymanager.repositoryservice.InventoryActionRepoService;

@Component
@RequiredArgsConstructor
public class InventoryActionTableCreatorImpl implements InventoryActionTableCreator {
    private static final String PATH_SAMPLE = "excelfiels/inventoryactions/%s.xlsx";
    private final InventoryActionRepoService inventoryActionRepoService;
    private final InventoryActionMapper inventoryActionMapper;
    private final InventoryActonTable inventoryActonTable;

    @Override
    @Transactional
    public void createTable(String email) {
        LocalDate today = LocalDate.now();
        List<InventoryAction> inventoryActionList = inventoryActionRepoService
                .getAllByUserEmilAndDateTime(email, today);

        List<InventoryActionExcelDto> actionExcelDtoList = inventoryActionList.stream()
                .map(inventoryActionMapper::toExcelDto)
                .toList();
        String filename = PATH_SAMPLE.formatted(email.toUpperCase() + "-" + today);
        inventoryActonTable.createExcel(actionExcelDtoList, filename);
    }
}
