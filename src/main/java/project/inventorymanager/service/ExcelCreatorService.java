package project.inventorymanager.service;

import project.inventorymanager.dto.excel.DatesDto;

public interface ExcelCreatorService {
    String createInventoryActionStatistic(DatesDto requestDto, String email);
}
