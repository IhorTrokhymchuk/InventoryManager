package project.inventorymanager.dto.inventory.response;

import lombok.Data;
import project.inventorymanager.dto.product.response.ProductResponseDto;
import project.inventorymanager.dto.warehouse.response.WarehouseResponseDto;

@Data
public class InventoryResponseDto {
    private Long id;
    private ProductResponseDto product;
    private WarehouseResponseDto warehouse;
    private Long quantity;
}
