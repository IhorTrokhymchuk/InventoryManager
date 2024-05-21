package project.inventorymanager.repositoryservice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.inventorymanager.model.inventory.Inventory;

public interface InventoryRepoService {
    Inventory save(Inventory inventory);

    Page<Inventory> findAllByUserEmail(Pageable pageable, String email);
}
