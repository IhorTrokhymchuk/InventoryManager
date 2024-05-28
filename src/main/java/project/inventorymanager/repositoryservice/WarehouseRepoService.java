package project.inventorymanager.repositoryservice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.inventorymanager.model.warehouse.Warehouse;

public interface WarehouseRepoService {
    Warehouse save(Warehouse warehouse);

    Warehouse getById(Long id);

    Page<Warehouse> findAll(Pageable pageable);

    void deleteById(Long id);
}
