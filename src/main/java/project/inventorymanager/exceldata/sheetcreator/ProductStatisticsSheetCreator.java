package project.inventorymanager.exceldata.sheetcreator;

import static project.inventorymanager.exceldata.ExcelStyleUtil.createBasicCellStyle;
import static project.inventorymanager.exceldata.ExcelStyleUtil.createDecimalCellStyle;
import static project.inventorymanager.exceldata.ExcelStyleUtil.createHeaderStyle;
import static project.inventorymanager.exceldata.ExcelUtil.autosizeColumns;
import static project.inventorymanager.exceldata.ExcelUtil.createCell;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

@Component
public class ProductStatisticsSheetCreator {
    private static final String SHEET_NAME = "Product Statistics";
    private static final short FONT_SIZE_10 = 10;
    private static final int DATA_START_ROW = 3;
    private static final Map<String, String> METRICS = Map.of(
            "Total REPLENISHMENT Quantity",
            "SUMIFS('%s'!F%s:F%s, '%s'!B%s:B%s, \"%s\", '%s'!G%s:G%s, \"REPLENISHMENT\")",
            "Total SHIPMENT Quantity",
            "SUMIFS('%s'!F%s:F%s, '%s'!B%s:B%s, \"%s\", '%s'!G%s:G%s, \"SHIPMENT\")",
            "Total REPLENISHMENT Amount",
            "SUMIFS('%s'!K%s:K%s, '%s'!B%s:B%s, \"%s\", '%s'!G%s:G%s, \"REPLENISHMENT\")",
            "Total SHIPMENT Amount",
            "SUMIFS('%s'!K%s:K%s, '%s'!B%s:B%s, \"%s\", '%s'!G%s:G%s, \"SHIPMENT\")",
            "Average Retail Price", "AVERAGEIFS('%s'!I%s:I%s, '%s'!B%s:B%s, \"%s\")",
            "Average Wholesale Price", "AVERAGEIFS('%s'!H%s:H%s, '%s'!B%s:B%s, \"%s\")",
            "Total Production Costs", "SUMIFS('%s'!K%s:K%s, '%s'!B%s:B%s, \"%s\")"
    );

    public void createSheet(Workbook workbook, Sheet dataSheet) {
        Sheet sheet = workbook.createSheet(SHEET_NAME);
        createHeader(sheet);
        createMetrics(sheet, dataSheet);
        autosizeColumns(sheet, METRICS.size() + 1);
    }

    private void createHeader(Sheet sheet) {
        CellStyle headerStyle = createHeaderStyle(sheet.getWorkbook(), FONT_SIZE_10);
        Row headerRow = sheet.createRow(0);
        createCell(headerRow, 0, "Product ID", headerStyle);
        int columnIndex = 1;
        for (String metricHeader : METRICS.keySet()) {
            createCell(headerRow, columnIndex++, metricHeader, headerStyle);
        }
    }

    private void createMetrics(Sheet sheet, Sheet dataSheet) {
        int lastRowNum = dataSheet.getLastRowNum() + 1;
        Set<Number> productIds = collectProductIds(dataSheet);
        CellStyle decimalCellStyle = createDecimalCellStyle(sheet.getWorkbook());
        CellStyle basicCellStyle = createBasicCellStyle(sheet.getWorkbook());
        int rowIndex = 1;
        for (Number productId : productIds) {
            Row row = sheet.createRow(rowIndex);
            createCell(row, 0, productId, basicCellStyle);
            int columnIndex = 1;
            for (String metricHeader : METRICS.keySet()) {
                Cell formulaCell = row.createCell(columnIndex);
                String formula = METRICS.get(metricHeader);
                formulaCell.setCellFormula(String.format(formula,
                        dataSheet.getSheetName(), DATA_START_ROW, lastRowNum,
                        dataSheet.getSheetName(), DATA_START_ROW, lastRowNum, productId.intValue(),
                        dataSheet.getSheetName(), DATA_START_ROW, lastRowNum));
                formulaCell.setCellStyle(decimalCellStyle);
                columnIndex++;
            }
            rowIndex++;
        }
    }

    private Set<Number> collectProductIds(Sheet dataSheet) {
        Set<Number> productIds = new HashSet<>();
        for (int rowIndex = DATA_START_ROW; rowIndex <= dataSheet.getLastRowNum(); rowIndex++) {
            Row row = dataSheet.getRow(rowIndex);
            if (row != null) {
                Cell productIdCell = row.getCell(1);
                if (productIdCell != null) {
                    productIds.add(productIdCell.getNumericCellValue());
                }
            }
        }
        return productIds;
    }
}
