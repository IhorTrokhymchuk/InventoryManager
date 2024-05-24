package project.inventorymanager.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import project.inventorymanager.model.inventory.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    @EntityGraph(attributePaths = {"product", "warehouse"})
    Page<Inventory> findAllByProductUserEmail(Pageable pageable, String email);

    Optional<Inventory> findByIdAndUserEmail(Long id, String email);

    Optional<Inventory> findByProductIdAndWarehouseIdAndUserEmail(
            Long productId, Long warehouseId, String email);
}
