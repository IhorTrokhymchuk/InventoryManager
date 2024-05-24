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
    public Inventory getByProductIdAndWarehouseIdAndUserEmail(
            Long productId, Long warehouseId, String email) {
        return inventoryRepository.findByProductIdAndWarehouseIdAndUserEmail(
                productId, warehouseId, email).orElseThrow(() -> new EntityNotFoundException(
                "Cant find product with id: " + productId
                        + " on warehouse with id: " + warehouseId
                        + " and user email: " + email));
    }

    @Override
    public Page<Inventory> findAllByUserEmail(Pageable pageable, String email) {
        return inventoryRepository.findAllByProductUserEmail(pageable, email);
    }

    @Override
    public Inventory getByIdAndUserEmail(Long id, String email) {
        return inventoryRepository.findByIdAndUserEmail(id, email).orElseThrow(
                () -> new EntityNotFoundException(
                        "Cant find inventory with id: " + id
                                + " user email: " + email));
    }

    @Override
    public Optional<Inventory> findByProductIdAndWarehouseIdAndUserEmail(
            Long productId, Long warehouseId, String email) {
        return inventoryRepository.findByProductIdAndWarehouseIdAndUserEmail(
                productId, warehouseId, email);
    }

}
