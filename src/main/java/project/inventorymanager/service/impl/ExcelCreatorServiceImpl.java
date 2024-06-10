package project.inventorymanager.service.impl;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.inventorymanager.dropbox.DropboxUtil;
import project.inventorymanager.dto.excel.DatesDto;
import project.inventorymanager.dto.inventoryaction.excel.InventoryActionExcelDto;
import project.inventorymanager.exceldata.filecreator.StatisticExcelCreator;
import project.inventorymanager.mapper.InventoryActionMapper;
import project.inventorymanager.model.file.StatisticFile;
import project.inventorymanager.model.user.User;
import project.inventorymanager.repositoryservice.FileRepositoryService;
import project.inventorymanager.repositoryservice.InventoryActionRepositoryService;
import project.inventorymanager.repositoryservice.UserRepositoryService;
import project.inventorymanager.service.ExcelCreatorService;
import project.inventorymanager.util.FileUtil;

@Component
@RequiredArgsConstructor
public class ExcelCreatorServiceImpl implements ExcelCreatorService {

    private final String directoryPath;
    private final InventoryActionRepositoryService inventoryActionRepositoryService;
    private final InventoryActionMapper inventoryActionMapper;
    private final StatisticExcelCreator statisticExcelCreatorTable;
    private final DropboxUtil dropboxUtil;
    private final FileRepositoryService fileRepositoryService;
    private final UserRepositoryService userRepositoryService;

    @Override
    @Transactional
    public String createInventoryActionStatistic(DatesDto requestDto, String email) {
        List<InventoryActionExcelDto> actionExcelDtoList
                = getActionExcelDtos(requestDto.getFromDate(), requestDto.getToDate());
        String excelFile
                = statisticExcelCreatorTable.createExcelFile(actionExcelDtoList, directoryPath);
        String dropboxId = dropboxUtil.uploadFile(excelFile);
        StatisticFile statisticFile = getStatisticFile(email, requestDto, dropboxId);
        fileRepositoryService.save(statisticFile);
        FileUtil.deleteFile(excelFile);
        return dropboxUtil.getDownloadUrl(dropboxId);
    }

    private List<InventoryActionExcelDto> getActionExcelDtos(LocalDate dateFrom, LocalDate dateTo) {
        return inventoryActionRepositoryService.getAllByDates(dateFrom, dateTo).stream()
                .map(inventoryActionMapper::toExcelDto)
                .toList();
    }

    private StatisticFile getStatisticFile(String email, DatesDto datesDto, String dropboxId) {
        StatisticFile statisticFile = new StatisticFile();
        User user = userRepositoryService.getByEmail(email);
        statisticFile.setUser(user);
        statisticFile.setCreatedAt(LocalDateTime.now());
        statisticFile.setDateFrom(datesDto.getFromDate());
        statisticFile.setDateTo(datesDto.getToDate());
        statisticFile.setDropboxId(dropboxId);
        return statisticFile;
    }
}
