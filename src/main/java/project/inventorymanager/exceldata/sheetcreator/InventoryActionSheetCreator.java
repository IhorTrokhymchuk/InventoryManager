package project.inventorymanager.exceldata.sheetcreator;

import static project.inventorymanager.exceldata.ExcelStyleUtil.createBasicCellStyle;
import static project.inventorymanager.exceldata.ExcelStyleUtil.createDateCellStyle;
import static project.inventorymanager.exceldata.ExcelStyleUtil.createDecimalCellStyle;
import static project.inventorymanager.exceldata.ExcelStyleUtil.createHeaderStyle;
import static project.inventorymanager.exceldata.ExcelUtil.autosizeColumns;
import static project.inventorymanager.exceldata.ExcelUtil.createCell;

import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Component;
import project.inventorymanager.dto.inventoryaction.excel.InventoryActionExcelDto;
import project.inventorymanager.exceldata.ExcelUtil;

@Component
public class InventoryActionSheetCreator {
    private static final int ZERO = 0;
    private static final short FONT_SIZE_14 = 13;
    private static final short FONT_SIZE_10 = 10;
    private static final int HEADER_ROW_INDEX = 1;
    private static final int DATA_START_ROW_INDEX = 2;
    private static final int TOTAL_CELL_INDEX = 10;
    private static final String TOTAL_FORMULA =
            "IF(%s=\"SHIPMENT\", %s*%s, IF(%s=\"REPLENISHMENT\", -(%s*%s), 0))";
    private static final String TITLE = "Inventory actions";
    private static final List<String> HEADERS = List.of(
            "Id", "Product id", "Product name", "Warehouse id",
            "Warehouse location", "Quantity", "Action type",
            "Wholesale price", "Retail price", "Date", "Total");

    public Sheet createSheet(List<InventoryActionExcelDto> excelDtoList, Workbook workbook) {
        Sheet sheet = workbook.createSheet("All Events");
        createMergedHeaderRow(sheet);
        createColumnHeaderRow(sheet);
        fillDataRows(excelDtoList, sheet);
        autosizeColumns(sheet, HEADERS.size());
        return sheet;
    }

    private void createMergedHeaderRow(Sheet sheet) {
        CellStyle headerStyle = createHeaderStyle(sheet.getWorkbook(), FONT_SIZE_14);
        Row titleRow = sheet.createRow(ZERO);
        createCell(titleRow, ZERO, TITLE, headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(ZERO, ZERO, ZERO, HEADERS.size() - 1));
    }

    private void createColumnHeaderRow(Sheet sheet) {
        CellStyle headerStyle = createHeaderStyle(sheet.getWorkbook(), FONT_SIZE_10);
        Row headerRow = sheet.createRow(HEADER_ROW_INDEX);
        for (int i = 0; i < HEADERS.size(); i++) {
            createCell(headerRow, i, HEADERS.get(i), headerStyle);
        }
    }

    private void fillDataRows(List<InventoryActionExcelDto> dataDto, Sheet sheet) {
        CellStyle style = createBasicCellStyle(sheet.getWorkbook());
        CellStyle dateStyle = createDateCellStyle(sheet.getWorkbook());
        CellStyle decimalCellStyle = createDecimalCellStyle(sheet.getWorkbook());

        int rowNum = DATA_START_ROW_INDEX;
        for (InventoryActionExcelDto dto : dataDto) {
            Row row = sheet.createRow(rowNum);
            createCell(row, 0, dto.getId(), style);
            createCell(row, 1, dto.getProductId(), style);
            createCell(row, 2, dto.getProductName(), style);
            createCell(row, 3, dto.getWarehouseId(), style);
            createCell(row, 4, dto.getWarehouseLocation(), style);
            createCell(row, 5, dto.getQuantity(), style);
            createCell(row, 6, dto.getActionTypeName(), style);
            createCell(row, 7, dto.getWholesalePrice().doubleValue(), decimalCellStyle);
            createCell(row, 8, dto.getRetailPrice().doubleValue(), decimalCellStyle);
            createCell(row, 9, ExcelUtil.convertToDate(dto.getCreatedAt()), dateStyle);
            insertTotalPriceFormula(rowNum, row, decimalCellStyle);
            rowNum++;
        }
    }

    private void insertTotalPriceFormula(int rowNum, Row row, CellStyle cellStyle) {
        String actionTypeCell = "G" + (rowNum + 1);
        String quantityCell = "F" + (rowNum + 1);
        String wholesalePriceCell = "H" + (rowNum + 1);
        String retailPriceCell = "I" + (rowNum + 1);

        Cell totalAmountCell = row.createCell(TOTAL_CELL_INDEX);
        String formula = String.format(TOTAL_FORMULA,
                actionTypeCell, quantityCell, retailPriceCell,
                actionTypeCell, quantityCell, wholesalePriceCell);
        totalAmountCell.setCellFormula(formula);
        totalAmountCell.setCellStyle(cellStyle);
    }
}
