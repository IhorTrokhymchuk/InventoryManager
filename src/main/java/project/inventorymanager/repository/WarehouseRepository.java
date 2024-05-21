package project.inventorymanager.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.inventorymanager.model.warehouse.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    Optional<Warehouse> findProductByIdAndUserEmail(Long id, String email);

    Page<Warehouse> findAllByUserEmail(Pageable pageable, String email);

    Integer deleteByIdAndUserEmail(Long id, String email);
}
