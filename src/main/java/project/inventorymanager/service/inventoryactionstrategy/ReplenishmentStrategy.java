package project.inventorymanager.service.inventoryactionstrategy;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.inventorymanager.dto.inventoryaction.request.InventoryActionRequestDto;
import project.inventorymanager.exception.warehouse.WarehouseDontHaveFreeCapacityException;
import project.inventorymanager.mapper.InventoryActionMapper;
import project.inventorymanager.model.inventory.Inventory;
import project.inventorymanager.model.inventoryaction.InventoryAction;
import project.inventorymanager.model.inventoryaction.InventoryActionType;
import project.inventorymanager.model.product.Product;
import project.inventorymanager.model.warehouse.Warehouse;
import project.inventorymanager.repositoryservice.InventoryActionRepositoryService;
import project.inventorymanager.repositoryservice.InventoryActionTypeRepositoryService;
import project.inventorymanager.repositoryservice.InventoryRepositoryService;
import project.inventorymanager.repositoryservice.ProductRepositoryService;
import project.inventorymanager.repositoryservice.WarehouseRepositoryService;

@Component
@RequiredArgsConstructor
public class ReplenishmentStrategy implements InventoryActionStrategy {
    private final InventoryActionTypeRepositoryService inventoryActionTypeRepositoryService;
    private final ProductRepositoryService productRepositoryService;
    private final WarehouseRepositoryService warehouseRepositoryService;
    private final InventoryActionRepositoryService inventoryActionRepositoryService;
    private final InventoryRepositoryService inventoryRepositoryService;
    private final InventoryActionMapper inventoryActionMapper;

    @Override
    public InventoryActionType.InventoryActionTypeName getType() {
        return InventoryActionType.InventoryActionTypeName.REPLENISHMENT;
    }

    @Override
    @Transactional
    public InventoryAction doAction(InventoryActionRequestDto requestDto) {
        InventoryAction inventoryAction = inventoryActionMapper.toModelWithQuantity(requestDto);
        Product product = productRepositoryService
                .getById(requestDto.getProductId());
        Warehouse warehouse = warehouseRepositoryService.getById(requestDto.getWarehouseId());
        setAndUpdateWarehouse(inventoryAction, warehouse, requestDto);
        updateOrCreateInventory(product, warehouse, requestDto.getQuantity());
        inventoryAction.setProduct(product);
        setInventoryActionType(inventoryAction);
        inventoryAction.setCreatedAt(LocalDateTime.now());
        setPrices(inventoryAction, product);
        return inventoryActionRepositoryService.save(inventoryAction);
    }

    private void setAndUpdateWarehouse(
            InventoryAction inventoryAction,
            Warehouse warehouse,
            InventoryActionRequestDto requestDto) {
        if (warehouse.getFreeCapacity() < requestDto.getQuantity()) {
            throw new WarehouseDontHaveFreeCapacityException(
                    "Warehouse with id: " + requestDto.getWarehouseId()
                            + " dont have capacity for " + requestDto.getQuantity()
                            + " products with id: " + requestDto.getProductId()
                            + ". Available free capacity: " + warehouse.getFreeCapacity());
        }
        warehouse.setFreeCapacity(warehouse.getFreeCapacity() - requestDto.getQuantity());
        warehouseRepositoryService.save(warehouse);
        inventoryAction.setWarehouse(warehouse);
    }

    private void updateOrCreateInventory(
            Product product, Warehouse warehouse, Long quantity) {
        Optional<Inventory> optionalInventory
                = inventoryRepositoryService.findByProductIdAndWarehouseId(
                        product.getId(), warehouse.getId());
        Inventory inventory;
        if (optionalInventory.isPresent()) {
            inventory = optionalInventory.get();
            inventory.setQuantity(inventory.getQuantity() + quantity);
        } else {
            inventory = new Inventory();
            inventory.setProduct(product);
            inventory.setWarehouse(warehouse);
            inventory.setQuantity(quantity);
        }
        inventoryRepositoryService.save(inventory);
    }

    private void setInventoryActionType(InventoryAction inventoryAction) {
        InventoryActionType actionType = inventoryActionTypeRepositoryService
                .getByTypeName(InventoryActionType.InventoryActionTypeName.REPLENISHMENT);
        inventoryAction.setInventoryActionType(actionType);
    }

    private void setPrices(InventoryAction inventoryAction, Product product) {
        inventoryAction.setRetailPrice(product.getRetailPrice());
        inventoryAction.setWholesalePrice(product.getWholesalePrice());
    }
}
