package project.inventorymanager.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.inventorymanager.model.inventoryaction.InventoryAction;

public interface InventoryActionRepository extends JpaRepository<InventoryAction, Long> {
    Optional<InventoryAction> findByIdAndUserEmail(Long id, String email);

    Page<InventoryAction> findAllByUserEmail(Pageable pageable, String email);
}
