package project.inventorymanager.repositoryservice;

import project.inventorymanager.model.inventoryaction.InventoryActionType;

public interface InventoryActionTypeRepoService {
    InventoryActionType save(InventoryActionType inventoryActionType);

    InventoryActionType getById(Long id);

    InventoryActionType getByTypeName(InventoryActionType.InventoryActionTypeName name);
}
