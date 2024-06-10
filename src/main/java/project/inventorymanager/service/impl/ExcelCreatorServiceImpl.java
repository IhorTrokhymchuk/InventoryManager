package project.inventorymanager.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
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
    private static final String SAMPLE_FILE_NAME = "%s-%s-%s.xlsx";
    private final String directoryPath;
    private final InventoryActionRepositoryService inventoryActionRepositoryService;
    private final InventoryActionMapper inventoryActionMapper;
    private final StatisticExcelCreator statisticExcelCreatorTable;
    private final DropboxUtil dropboxUtil;
    private final FileRepositoryService fileRepositoryService;
    private final UserRepositoryService userRepositoryService;

    @Override
    public String createInventoryActionStatistic(DatesDto requestDto, String email) {
        //for sample
        LocalDate dateFrom = requestDto.getFromDate();
        LocalDate dateTo = requestDto.getToDate();

        //name creating
        String id = generateUniqueId();
        String filename = directoryPath + SAMPLE_FILE_NAME.formatted(id, dateFrom, dateTo);

        //get all actions by date
        List<InventoryActionExcelDto> actionExcelDtoList =
                inventoryActionRepositoryService.getAllByDates(dateFrom, dateTo).stream()
                .map(inventoryActionMapper::toExcelDto)
                .toList();

        //generate file
        statisticExcelCreatorTable.createExcelFile(actionExcelDtoList, filename);

        //upload to drop box and get uniq id
        String dropboxId = dropboxUtil.uploadFile(filename);

        //generate file entity
        StatisticFile statisticFile = new StatisticFile();
        User user = userRepositoryService.getByEmail(email);
        statisticFile.setUser(user);
        statisticFile.setCreatedAt(LocalDateTime.now());
        statisticFile.setDateFrom(dateFrom);
        statisticFile.setDateTo(dateTo);
        statisticFile.setDropboxId(dropboxId);

        //save entity to db
        fileRepositoryService.save(statisticFile);

        //delete local file
        FileUtil.deleteFile(filename);

        //get url to dropbox file and return
        return dropboxUtil.getDownloadUrl(dropboxId);

    }

    private String generateUniqueId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
