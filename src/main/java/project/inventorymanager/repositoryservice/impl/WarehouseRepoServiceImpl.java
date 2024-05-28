package project.inventorymanager.repositoryservice.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.inventorymanager.exception.repository.EntityNotFoundException;
import project.inventorymanager.model.warehouse.Warehouse;
import project.inventorymanager.repository.WarehouseRepository;
import project.inventorymanager.repositoryservice.WarehouseRepoService;

@Service
@RequiredArgsConstructor
public class WarehouseRepoServiceImpl implements WarehouseRepoService {
    private final WarehouseRepository warehouseRepository;

    @Override
    public Warehouse save(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }

    @Override
    public Warehouse getById(Long id) {
        return warehouseRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Cant find warehouse with id: " + id)
        );
    }

    @Override
    public Page<Warehouse> findAll(Pageable pageable) {
        return warehouseRepository.findAll(pageable);
    }

    @Override
    public void deleteById(Long id) {
        boolean existsById = warehouseRepository.existsById(id);
        if (existsById) {
            warehouseRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Cant find warehouse with id: " + id);
        }
    }
}
