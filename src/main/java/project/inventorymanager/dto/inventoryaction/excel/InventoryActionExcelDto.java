package project.inventorymanager.dto.inventoryaction.excel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class InventoryActionExcelDto {
    private Long id;
    private Long productId;
    private String productName;
    private Long warehouseId;
    private String warehouseLocation;
    private Long quantity;
    private String actionTypeName;
    private BigDecimal wholesalePrice;
    private BigDecimal retailPrice;
    private LocalDateTime createdAt;
}
