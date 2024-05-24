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
    public Warehouse getByIdIfUserHavePermission(Long id, String email) {
        return warehouseRepository.findProductByIdAndUserEmail(id, email).orElseThrow(
                () -> new EntityNotFoundException("Cant find warehouse with user email: " + email
                        + " and id: " + id)
        );
    }

    @Override
    public Page<Warehouse> findAllByUserEmail(Pageable pageable, String email) {
        return warehouseRepository.findAllByUserEmail(pageable, email);
    }

    @Override
    public void deleteByIdIfUserHavePermission(Long id, String email) {
        Integer result = warehouseRepository.deleteByIdAndUserEmail(id, email);
        if (result == 0) {
            throw new EntityNotFoundException(
                    "Cant delete warehouse with user email: " + email + " and id: " + id);
        }
    }
}
