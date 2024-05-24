package project.inventorymanager.repositoryservice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.inventorymanager.model.inventoryaction.InventoryAction;

public interface InventoryActionRepoService {
    InventoryAction save(InventoryAction inventoryAction);

    InventoryAction getByIdIfUserHavePermission(Long id, String email);

    Page<InventoryAction> findAllByUserEmail(Pageable pageable, String email);
}
