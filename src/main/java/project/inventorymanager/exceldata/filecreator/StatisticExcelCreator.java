package project.inventorymanager.exceldata.filecreator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
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

@Component
@RequiredArgsConstructor
public class StatisticExcelCreator {
    private final InventoryActionSheetCreator inventoryActionSheetCreator;
    private final InventoryActionStatisticSheetCreator inventoryActionStatisticSheetCreator;
    private final ProductStatisticsSheetCreator productStatisticsSheetCreator;
    
    public void createExcelFile(List<InventoryActionExcelDto> excelDtoList, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet dataSheet = inventoryActionSheetCreator.createSheet(excelDtoList, workbook);
            inventoryActionStatisticSheetCreator.createSheet(workbook, dataSheet);
            productStatisticsSheetCreator.createSheet(workbook, dataSheet);
            writeToFile(workbook, filePath);
        } catch (IOException e) {
            throw new WorkWithFileExceptions(
                    "Error creating or writing to the Excel file: " + filePath
                            + " " + e.getMessage());
        }
    }
    
    private void writeToFile(Workbook workbook, String filePath) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }
    }
}
