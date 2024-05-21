package project.inventorymanager.repositoryservice.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.inventorymanager.model.inventory.Inventory;
import project.inventorymanager.repository.InventoryRepository;
import project.inventorymanager.repositoryservice.InventoryRepoService;

@Service
@RequiredArgsConstructor
public class InventoryRepoServiceImpl implements InventoryRepoService {
    private final InventoryRepository inventoryRepository;

    @Override
    public Inventory save(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    @Override
    public Page<Inventory> findAllByUserEmail(Pageable pageable, String email) {
        return inventoryRepository.findAllByProductUserEmail(pageable, email);
    }
}
