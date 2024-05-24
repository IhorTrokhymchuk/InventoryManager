package project.inventorymanager.repositoryservice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.inventorymanager.model.warehouse.Warehouse;

public interface WarehouseRepoService {
    Warehouse save(Warehouse warehouse);

    Warehouse getByIdIfUserHavePermission(Long id, String email);

    Page<Warehouse> findAllByUserEmail(Pageable pageable, String email);

    void deleteByIdIfUserHavePermission(Long id, String email);
}
