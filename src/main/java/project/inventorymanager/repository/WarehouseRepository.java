package project.inventorymanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.inventorymanager.model.warehouse.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
}
