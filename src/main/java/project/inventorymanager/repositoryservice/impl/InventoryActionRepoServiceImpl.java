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
import project.inventorymanager.repositoryservice.InventoryActionRepoService;

@Service
@RequiredArgsConstructor
public class InventoryActionRepoServiceImpl implements InventoryActionRepoService {
    private final InventoryActionRepository inventoryActionRepository;

    @Override
    public InventoryAction save(InventoryAction inventoryAction) {
        return inventoryActionRepository.save(inventoryAction);
    }

    @Override
    public InventoryAction getByIdIfUserHavePermission(Long id, String email) {
        return inventoryActionRepository.findByIdAndUserEmail(id, email).orElseThrow(
                () -> new EntityNotFoundException(
                        "Cant find inventory action with user email: " + email + " and id: " + id));
    }

    @Override
    public Page<InventoryAction> findAllByUserEmail(Pageable pageable, String email) {
        return inventoryActionRepository.findAllByUserEmail(pageable, email);
    }

    @Override
    public List<InventoryAction> getAllByUserEmilAndDateTime(
            String email, LocalDate localDate) {
        List<InventoryAction> actions = inventoryActionRepository
                .findAllByUserEmailAndCreatedAtDate(email, localDate);
        if (actions.isEmpty()) {
            throw new EntityNotFoundException("Cant find inventory actions by user email: " + email
                            + " and date: " + localDate);
        }
        return actions;
    }
}
