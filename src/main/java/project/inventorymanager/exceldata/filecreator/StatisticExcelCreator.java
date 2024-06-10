package project.inventorymanager.exceldata.filecreator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import project.inventorymanager.dto.inventoryaction.excel.InventoryActionExcelDto;
import project.inventorymanager.exceldata.sheetcreator.InventoryActionSheetCreator;
import project.inventorymanager.exceldata.sheetcreator.InventoryActionStatisticSheetCreator;
import project.inventorymanager.exceldata.sheetcreator.ProductStatisticsSheetCreator;
import project.inventorymanager.exception.file.WorkWithFileExceptions;
import project.inventorymanager.util.FileUtil;

@Component
@RequiredArgsConstructor
public class StatisticExcelCreator {
    private static final String SAMPLE_FILE_NAME = "%s.xlsx";
    private final InventoryActionSheetCreator inventoryActionSheetCreator;
    private final InventoryActionStatisticSheetCreator inventoryActionStatisticSheetCreator;
    private final ProductStatisticsSheetCreator productStatisticsSheetCreator;
    
    public String createExcelFile(
            List<InventoryActionExcelDto> excelDtoList, String directoryPath) {
        String fileName = getFileName(directoryPath);
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet dataSheet = inventoryActionSheetCreator.createSheet(excelDtoList, workbook);
            inventoryActionStatisticSheetCreator.createSheet(workbook, dataSheet);
            productStatisticsSheetCreator.createSheet(workbook, dataSheet);
            writeToFile(workbook, fileName);
            return fileName;
        } catch (IOException e) {
            FileUtil.clearDirectory(directoryPath);
            throw new WorkWithFileExceptions(
                    "Error creating or writing to the Excel file: " + fileName
                            + " " + e.getMessage());
        }
    }

    private String getFileName(String directoryPath) {
        UUID uuid = UUID.randomUUID();
        return directoryPath + SAMPLE_FILE_NAME.formatted(uuid);
    }
    
    private void writeToFile(Workbook workbook, String filePath) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }
    }
}
