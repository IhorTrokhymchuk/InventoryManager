package project.inventorymanager.testutil.objects;

import static project.inventorymanager.testutil.objects.ProductProvider.getProduct;
import static project.inventorymanager.testutil.objects.ProductProvider.getProductResponseDto;
import static project.inventorymanager.testutil.objects.WarehouseProvider.getWarehouse;
import static project.inventorymanager.testutil.objects.WarehouseProvider.getWarehouseResponseDto;

import project.inventorymanager.dto.inventory.response.InventoryResponseDto;
import project.inventorymanager.model.inventory.Inventory;

public class InventoryProvider {
    private static final Long QUANTITY = 10L;

    public static Inventory getInventory(Long id) {
        Inventory inventory = new Inventory();
        inventory.setId(id);
        inventory.setQuantity(QUANTITY + id);
        inventory.setProduct(getProduct(id));
        inventory.setWarehouse(getWarehouse(id));
        return inventory;
    }

    public static InventoryResponseDto getInventoryResponseDto(Long id) {
        InventoryResponseDto inventoryResponseDto = new InventoryResponseDto();
        inventoryResponseDto.setId(id);
        inventoryResponseDto.setQuantity(QUANTITY + id);
        inventoryResponseDto.setProduct(getProductResponseDto(id));
        inventoryResponseDto.setWarehouse(getWarehouseResponseDto(id));
        return inventoryResponseDto;
    }
}
