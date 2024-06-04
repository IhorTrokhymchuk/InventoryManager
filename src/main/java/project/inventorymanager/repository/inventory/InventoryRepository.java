package project.inventorymanager.repository.inventory;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import project.inventorymanager.model.inventory.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    @EntityGraph(attributePaths = {"product", "product.categories", "warehouse"})
    Page<Inventory> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"product", "product.categories", "warehouse"})
    Optional<Inventory> findById(Long id);

    Optional<Inventory> findByProductIdAndWarehouseId(Long productId, Long warehouseId);
}
