package project.inventorymanager.exceldata.sheetcreator;

import static project.inventorymanager.exceldata.ExcelStyleUtil.createDecimalCellStyle;
import static project.inventorymanager.exceldata.ExcelStyleUtil.createHeaderStyle;
import static project.inventorymanager.exceldata.ExcelUtil.autosizeColumns;
import static project.inventorymanager.exceldata.ExcelUtil.createCell;

import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

@Component
public class InventoryActionStatisticSheetCreator {
    private static final String SHEET_NAME = "Inventory Action statistic";
    private static final short FONT_SIZE_10 = 10;
    private static final int DATA_START_ROW = 3;
    private static final Map<String, String> METRICS = Map.of(
            "Total REPLENISHMENT Quantity", "SUMIF('%s'!G%s:G%s, \"REPLENISHMENT\", '%s'!F%s:F%s)",
            "Total SHIPMENT Quantity", "SUMIF('%s'!G%s:G%s, \"SHIPMENT\", '%s'!F%s:F%s)",
            "Total REPLENISHMENT Amount", "SUMIF('%s'!G%s:G%s, \"REPLENISHMENT\", '%s'!K%s:K%s)",
            "Total SHIPMENT Amount", "SUMIF('%s'!G%s:G%s, \"SHIPMENT\", '%s'!K%s:K%s)",
            "Average Retail Price", "AVERAGE('%s'!I%s:I%s)",
            "Average Wholesale Price", "AVERAGE('%s'!H%s:H%s)",
            "Total Production Costs", "SUM('%s'!K%s:K%s)"
    );

    public void createSheet(Workbook workbook, Sheet dataSheet) {
        Sheet sheet = workbook.createSheet(SHEET_NAME);
        createMetrics(sheet, dataSheet);
        autosizeColumns(sheet, METRICS.size());
    }

    private void createMetrics(Sheet sheet, Sheet dataSheet) {
        int lastDataRow = Math.max(dataSheet.getLastRowNum(), DATA_START_ROW) + 1;
        CellStyle headerStyle = createHeaderStyle(sheet.getWorkbook(), FONT_SIZE_10);
        CellStyle decimalCellStyle = createDecimalCellStyle(sheet.getWorkbook());
        int rowIndex = 0;
        for (String metricHeader : METRICS.keySet()) {
            Row row = sheet.createRow(rowIndex);
            createCell(row, 0, metricHeader, headerStyle);
            Cell formulaCell = row.createCell(1);
            String formula = METRICS.get(metricHeader);
            formulaCell.setCellFormula(String.format(formula,
                    dataSheet.getSheetName(), DATA_START_ROW, lastDataRow,
                    dataSheet.getSheetName(), DATA_START_ROW, lastDataRow));
            formulaCell.setCellStyle(decimalCellStyle);
            rowIndex++;
        }
    }
}
