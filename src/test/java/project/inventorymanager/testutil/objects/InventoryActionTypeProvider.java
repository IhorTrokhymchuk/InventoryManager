package project.inventorymanager.testutil.objects;

import static project.inventorymanager.model.inventoryaction.InventoryActionType.InventoryActionTypeName.REPLENISHMENT;
import static project.inventorymanager.model.inventoryaction.InventoryActionType.InventoryActionTypeName.SHIPMENT;

import project.inventorymanager.dto.inventoryactiontype.response.InventoryActionTypeResponseDto;
import project.inventorymanager.model.inventoryaction.InventoryActionType;

public class InventoryActionTypeProvider {

    public static InventoryActionType getInventoryActionTypeById(Long id) {
        if (id == 1L) {
            return getShipmentType();
        } else if (id == 2) {
            return getReplenishmentType();
        }
        throw new RuntimeException("Dont have InventoryActionType with id: " + id);
    }

    public static InventoryActionTypeResponseDto getInventoryActionTypeResponseDtoById(Long id) {
        if (id == 1L) {
            return getShipmentTypeResponseDto();
        } else if (id == 2) {
            return getReplenishmentTypeResponseDto();
        }
        throw new RuntimeException("Dont have InventoryActionTypeResponseDto with id: " + id);
    }

    private static InventoryActionTypeResponseDto getShipmentTypeResponseDto() {
        InventoryActionTypeResponseDto inventoryActionTypeResponseDto
                = new InventoryActionTypeResponseDto();
        inventoryActionTypeResponseDto.setId(1L);
        inventoryActionTypeResponseDto.setName(SHIPMENT.name());
        return inventoryActionTypeResponseDto;
    }

    private static InventoryActionTypeResponseDto getReplenishmentTypeResponseDto() {
        InventoryActionTypeResponseDto inventoryActionTypeResponseDto
                = new InventoryActionTypeResponseDto();
        inventoryActionTypeResponseDto.setId(2L);
        inventoryActionTypeResponseDto.setName(REPLENISHMENT.name());
        return inventoryActionTypeResponseDto;
    }

    private static InventoryActionType getShipmentType() {
        InventoryActionType inventoryActionType = new InventoryActionType();
        inventoryActionType.setId(1L);
        inventoryActionType.setName(SHIPMENT);
        return inventoryActionType;
    }

    private static InventoryActionType getReplenishmentType() {
        InventoryActionType inventoryActionType = new InventoryActionType();
        inventoryActionType.setId(2L);
        inventoryActionType.setName(REPLENISHMENT);
        return inventoryActionType;
    }
}
