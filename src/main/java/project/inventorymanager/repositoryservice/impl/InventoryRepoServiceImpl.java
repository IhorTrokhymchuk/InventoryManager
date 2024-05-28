package project.inventorymanager.repositoryservice.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.inventorymanager.exception.repository.EntityNotFoundException;
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
    public Inventory getByProductIdAndWarehouseId(Long productId, Long warehouseId) {
        return inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cant find product with id: " + productId
                                + " on warehouse with id: " + warehouseId));
    }

    @Override
    public Page<Inventory> findAll(Pageable pageable) {
        return inventoryRepository.findAll(pageable);
    }

    @Override
    public Inventory getById(Long id) {
        return inventoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        "Cant find inventory with id: " + id));
    }

    @Override
    public Optional<Inventory> findByProductIdAndWarehouseId(Long productId, Long warehouseId) {
        return inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId);
    }
}
