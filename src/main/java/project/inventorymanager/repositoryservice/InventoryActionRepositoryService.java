package project.inventorymanager.repositoryservice;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.inventorymanager.model.inventoryaction.InventoryAction;

public interface InventoryActionRepositoryService {
    InventoryAction save(InventoryAction inventoryAction);

    InventoryAction getById(Long id);

    Page<InventoryAction> findAll(Pageable pageable);

    List<InventoryAction> getAllByDates(LocalDate fromLocalDate, LocalDate toLocalDate);
}
