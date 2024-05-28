package project.inventorymanager.service;

import project.inventorymanager.dto.excel.DatesDto;

public interface ExcelCreatorService {
    void createInventoryActionStatistic(DatesDto requestDto);
}
