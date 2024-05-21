package project.inventorymanager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.inventorymanager.model.inventory.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Page<Inventory> findAllByProductUserEmail(Pageable pageable, String email);
}
