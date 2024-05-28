package project.inventorymanager.service.impl;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.inventorymanager.dto.excel.DatesDto;
import project.inventorymanager.dto.inventoryaction.excel.InventoryActionExcelDto;
import project.inventorymanager.exceldata.filecreator.StatisticExcelCreator;
import project.inventorymanager.mapper.InventoryActionMapper;
import project.inventorymanager.model.inventoryaction.InventoryAction;
import project.inventorymanager.repositoryservice.InventoryActionRepoService;
import project.inventorymanager.service.ExcelCreatorService;

@Component
@RequiredArgsConstructor
public class ExcelCreatorServiceImpl implements ExcelCreatorService {
    private static final String SAMPLE_FILE_NAME = "%s-%s-%s.xlsx";
    private final String filePath;
    private final InventoryActionRepoService inventoryActionRepoService;
    private final InventoryActionMapper inventoryActionMapper;
    private final StatisticExcelCreator statisticExcelCreatorTable;

    @Override
    @Transactional
    public void createInventoryActionStatistic(String email, DatesDto requestDto) {
        LocalDate fromDate = requestDto.getFromDate();
        LocalDate toDate = requestDto.getToDate();
        List<InventoryAction> inventoryActionList = inventoryActionRepoService
                .getAllByUserEmilAndDateTime(email, fromDate, toDate);
        List<InventoryActionExcelDto> actionExcelDtoList = inventoryActionList.stream()
                .map(inventoryActionMapper::toExcelDto)
                .toList();
        String filename = filePath + SAMPLE_FILE_NAME.formatted(email, fromDate, toDate);
        statisticExcelCreatorTable.createExcelFile(actionExcelDtoList, filename);
    }
}
