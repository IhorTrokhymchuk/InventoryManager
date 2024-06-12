package project.inventorymanager.repositoryservice;

import project.inventorymanager.model.inventoryaction.InventoryActionType;

public interface InventoryActionTypeRepositoryService {
    InventoryActionType getById(Long id);

    InventoryActionType getByTypeName(InventoryActionType.InventoryActionTypeName name);
}
