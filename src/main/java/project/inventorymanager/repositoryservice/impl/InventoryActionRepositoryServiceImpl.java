package project.inventorymanager.repositoryservice.impl;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.inventorymanager.exception.repository.EntityNotFoundException;
import project.inventorymanager.model.inventoryaction.InventoryAction;
import project.inventorymanager.repository.InventoryActionRepository;
import project.inventorymanager.repositoryservice.InventoryActionRepositoryService;

@Service
@RequiredArgsConstructor
public class InventoryActionRepositoryServiceImpl implements InventoryActionRepositoryService {
    private final InventoryActionRepository inventoryActionRepository;

    @Override
    public InventoryAction save(InventoryAction inventoryAction) {
        return inventoryActionRepository.save(inventoryAction);
    }

    @Override
    public InventoryAction getById(Long id) {
        return inventoryActionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        "Cant find inventory action with id: " + id));
    }

    @Override
    public Page<InventoryAction> findAll(Pageable pageable) {
        return inventoryActionRepository.findAll(pageable);
    }

    @Override
    public List<InventoryAction> getAllByDates(
            LocalDate fromDate, LocalDate toDate) {
        List<InventoryAction> actions = inventoryActionRepository.findAllByDates(fromDate, toDate);
        if (actions.isEmpty()) {
            throw new EntityNotFoundException("Cant find inventory actions by from date: "
                    + fromDate + " to date: " + toDate);
        }
        return actions;
    }
}
