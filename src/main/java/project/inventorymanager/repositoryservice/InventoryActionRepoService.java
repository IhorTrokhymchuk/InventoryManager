package project.inventorymanager.repositoryservice;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.inventorymanager.model.inventoryaction.InventoryAction;

public interface InventoryActionRepoService {
    InventoryAction save(InventoryAction inventoryAction);

    InventoryAction getByIdIfUserHavePermission(Long id, String email);

    Page<InventoryAction> findAllByUserEmail(Pageable pageable, String email);

    List<InventoryAction> getAllByUserEmilAndDateTime(String email, LocalDate localDate);
}
