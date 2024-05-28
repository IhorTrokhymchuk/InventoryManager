package project.inventorymanager.exceldata;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

@Component
public class ExcelStyleUtil {

    public static CellStyle createHeaderStyle(Workbook workbook, short fontSize) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints(fontSize);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        addBorders(style);
        return style;
    }

    public static CellStyle createBasicCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        addBorders(style);
        return style;
    }

    public static CellStyle createDateCellStyle(Workbook workbook) {
        CellStyle style = createBasicCellStyle(workbook);
        style.setDataFormat(workbook.getCreationHelper()
                .createDataFormat().getFormat("yyyy-mm-dd hh:mm:ss"));
        return style;
    }

    public static CellStyle createDecimalCellStyle(Workbook workbook) {
        CellStyle style = createBasicCellStyle(workbook);
        style.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("#,##0.00"));
        return style;
    }

    private static void addBorders(CellStyle style) {
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }
}
