package project.inventorymanager.dto.inventoryaction.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InventoryActionRequestDto {
    @NotNull
    private Long productId;
    @NotNull
    private Long warehouseId;
    @NotNull
    private Long quantity;
    @NotNull
    private Long inventoryActionTypeId;
}
