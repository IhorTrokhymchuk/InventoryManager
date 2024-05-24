package project.inventorymanager.service.inventoryactionstrategy;

import project.inventorymanager.dto.inventoryaction.request.InventoryActionRequestDto;
import project.inventorymanager.model.inventoryaction.InventoryAction;
import project.inventorymanager.model.inventoryaction.InventoryActionType;

public interface InventoryActionStrategy {
    InventoryActionType.InventoryActionTypeName getType();

    InventoryAction doAction(InventoryActionRequestDto requestDto, String email);
}
