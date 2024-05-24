package project.inventorymanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.inventorymanager.model.inventoryaction.InventoryActionType;

public interface InventoryActionTypeRepository extends JpaRepository<InventoryActionType, Long> {
    InventoryActionType findInventoryActionTypeByName(
            InventoryActionType.InventoryActionTypeName name);
}
