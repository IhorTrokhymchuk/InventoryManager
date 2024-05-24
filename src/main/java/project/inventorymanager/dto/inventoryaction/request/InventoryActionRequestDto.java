package project.inventorymanager.dto.inventoryaction.request;

import lombok.Data;

@Data
public class InventoryActionRequestDto {
    private Long productId;
    private Long warehouseId;
    private Long quantity;
    private Long inventoryActionTypeId;
}
