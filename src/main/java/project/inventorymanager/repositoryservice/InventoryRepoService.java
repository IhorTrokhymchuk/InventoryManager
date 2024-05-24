package project.inventorymanager.repositoryservice;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.inventorymanager.model.inventory.Inventory;

public interface InventoryRepoService {
    Inventory save(Inventory inventory);

    Inventory getByProductIdAndWarehouseIdAndUserEmail(
            Long productId, Long warehouseId, String email);

    Page<Inventory> findAllByUserEmail(Pageable pageable, String email);

    Inventory getByIdAndUserEmail(Long id, String email);

    Optional<Inventory> findByProductIdAndWarehouseIdAndUserEmail(
            Long productId, Long warehouseId, String email);

}
