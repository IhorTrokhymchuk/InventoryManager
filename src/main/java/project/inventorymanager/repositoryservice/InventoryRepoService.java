package project.inventorymanager.repositoryservice;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.inventorymanager.model.inventory.Inventory;

public interface InventoryRepoService {
    Inventory save(Inventory inventory);

    Inventory getByProductIdAndWarehouseId(Long productId, Long warehouseId);

    Page<Inventory> findAll(Pageable pageable);

    Inventory getById(Long id);

    Optional<Inventory> findByProductIdAndWarehouseId(Long productId, Long warehouseId);

}
