package project.inventorymanager.dto.inventoryaction.response;

import java.time.LocalDateTime;
import lombok.Data;
import project.inventorymanager.dto.inventoryactiontype.response.InventoryActionTypeResponseDto;
import project.inventorymanager.dto.product.response.ProductResponseDto;
import project.inventorymanager.dto.warehouse.response.WarehouseResponseDto;

@Data
public class InventoryActionResponseDto {
    private Long id;
    private ProductResponseDto product;
    private WarehouseResponseDto warehouse;
    private Long quantity;
    private InventoryActionTypeResponseDto inventoryActionType;
    private LocalDateTime createdAt;
}
