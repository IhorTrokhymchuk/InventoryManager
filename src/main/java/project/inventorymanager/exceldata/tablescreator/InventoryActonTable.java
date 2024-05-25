package project.inventorymanager.exceldata.tablescreator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import project.inventorymanager.dto.inventoryaction.excel.InventoryActionExcelDto;

@Component
public class InventoryActonTable {
    private static final String[] HEADERS = {
            "id", "product_id", "product_name", "warehouse_id",
            "warehouse_location", "quantity", "action_type_name",
            "wholesale_price", "retail_price", "created_at", "total_amount"};

    public void createExcel(List<InventoryActionExcelDto> excelDtoList, String filePath) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("InventoryAction");
        createHeaderRow(sheet);
        fillDataRows(excelDtoList, sheet);
        autosizeColumn(sheet);
        writeToFile(workbook, filePath);
        closeFile(workbook);
    }

    private void createHeaderRow(Sheet sheet) {

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < HEADERS.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(HEADERS[i]);
        }
    }

    private void insertTotalPriceFormula(int rowNum, Row row) {
        String actionTypeCell = "G" + (rowNum + 1);
        String quantityCell = "F" + (rowNum + 1);
        String wholesalePriceCell = "H" + (rowNum + 1);
        String retailPriceCell = "I" + (rowNum + 1);

        Cell totalAmountCell = row.createCell(10);
        String formula = String.format(
                "IF(%s=\"SHIPMENT\", %s*%s, IF(%s=\"REPLENISHMENT\", -(%s*%s), 0))",
                actionTypeCell, quantityCell, wholesalePriceCell,
                actionTypeCell, quantityCell, retailPriceCell);
        totalAmountCell.setCellFormula(formula);
    }

    private void autosizeColumn(Sheet sheet) {
        for (int i = 0; i < HEADERS.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void writeToFile(Workbook workbook, String filePath) {
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            //todo: normalize catch and delete
            e.printStackTrace();
        }
    }

    private void closeFile(Workbook workbook) {
        try {
            workbook.close();
        } catch (IOException e) {
            //todo: normalize catch and delete
            e.printStackTrace();
        }
    }

    private void fillDataRows(
            List<InventoryActionExcelDto> inventoryActionExcelDtoList, Sheet sheet) {
        int rowNum = 1;
        for (InventoryActionExcelDto dto : inventoryActionExcelDtoList) {
            Row row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(dto.getId());
            row.createCell(1).setCellValue(dto.getProductId());
            row.createCell(2).setCellValue(dto.getProductName());
            row.createCell(3).setCellValue(dto.getWarehouseId());
            row.createCell(4).setCellValue(dto.getWarehouseLocation());
            row.createCell(5).setCellValue(dto.getQuantity());
            row.createCell(6).setCellValue(dto.getActionTypeName());
            row.createCell(7).setCellValue(dto.getWholesalePrice().doubleValue());
            row.createCell(8).setCellValue(dto.getRetailPrice().doubleValue());
            row.createCell(9).setCellValue(dto.getCreatedAt().toLocalDate());
            insertTotalPriceFormula(rowNum, row);
            rowNum++;
        }
    }
}
