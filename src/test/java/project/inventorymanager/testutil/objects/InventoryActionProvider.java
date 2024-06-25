package project.inventorymanager.testutil.objects;

import static project.inventorymanager.testutil.objects.InventoryActionTypeProvider.getInventoryActionTypeById;
import static project.inventorymanager.testutil.objects.InventoryActionTypeProvider.getInventoryActionTypeResponseDtoById;
import static project.inventorymanager.testutil.objects.ProductProvider.getProduct;
import static project.inventorymanager.testutil.objects.ProductProvider.getProductResponseDto;
import static project.inventorymanager.testutil.objects.WarehouseProvider.getWarehouse;
import static project.inventorymanager.testutil.objects.WarehouseProvider.getWarehouseResponseDto;

import java.time.LocalDateTime;
import project.inventorymanager.dto.inventoryaction.request.InventoryActionRequestDto;
import project.inventorymanager.dto.inventoryaction.response.InventoryActionResponseDto;
import project.inventorymanager.dto.product.response.ProductResponseDto;
import project.inventorymanager.model.inventoryaction.InventoryAction;
import project.inventorymanager.model.product.Product;

public class InventoryActionProvider {
    private static final Long QUANTITY = 10L;

    public static InventoryAction getInventoryAction(Long id, Long actionTypeId) {
        InventoryAction inventoryAction = new InventoryAction();
        inventoryAction.setId(id);
        Product product = getProduct(id);
        inventoryAction.setProduct(product);
        inventoryAction.setWarehouse(getWarehouse(id));
        inventoryAction.setQuantity(QUANTITY + id);
        inventoryAction.setInventoryActionType(getInventoryActionTypeById(actionTypeId));
        inventoryAction.setCreatedAt(LocalDateTime.now());
        inventoryAction.setWholesalePrice(product.getWholesalePrice());
        inventoryAction.setRetailPrice(product.getRetailPrice());
        return inventoryAction;
    }

    public static InventoryActionRequestDto getInventoryActionRequestDto(
            Long id, Long actionTypeId) {
        InventoryActionRequestDto inventoryActionRequestDto = new InventoryActionRequestDto();
        inventoryActionRequestDto.setProductId(id);
        inventoryActionRequestDto.setWarehouseId(id);
        inventoryActionRequestDto.setQuantity(QUANTITY + id);
        inventoryActionRequestDto.setInventoryActionTypeId(actionTypeId);
        return inventoryActionRequestDto;
    }

    public static InventoryActionResponseDto getInventoryActionResponseDto(
            Long id, Long actionTypeId) {
        InventoryActionResponseDto inventoryActionResponseDto = new InventoryActionResponseDto();
        inventoryActionResponseDto.setId(id);
        ProductResponseDto productResponseDto = getProductResponseDto(id);
        inventoryActionResponseDto.setProduct(productResponseDto);
        inventoryActionResponseDto.setWarehouse(getWarehouseResponseDto(id));
        inventoryActionResponseDto.setQuantity(QUANTITY + id);
        inventoryActionResponseDto.setInventoryActionType(
                getInventoryActionTypeResponseDtoById(actionTypeId));
        inventoryActionResponseDto.setCreatedAt(LocalDateTime.now());
        inventoryActionResponseDto.setWholesalePrice(productResponseDto.getWholesalePrice());
        inventoryActionResponseDto.setRetailPrice(productResponseDto.getRetailPrice());
        return inventoryActionResponseDto;
    }
}
