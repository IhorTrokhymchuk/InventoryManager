package project.inventorymanager.service.inventoryactionstrategy;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.inventorymanager.dto.inventoryaction.request.InventoryActionRequestDto;
import project.inventorymanager.exception.inventory.InventoryQuantityException;
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
public class ShipmentStrategy implements InventoryActionStrategy {
    private final InventoryActionTypeRepositoryService inventoryActionTypeRepositoryService;
    private final ProductRepositoryService productRepositoryService;
    private final WarehouseRepositoryService warehouseRepositoryService;
    private final InventoryActionRepositoryService inventoryActionRepositoryService;
    private final InventoryRepositoryService inventoryRepositoryService;
    private final InventoryActionMapper inventoryActionMapper;

    @Override
    public InventoryActionType.InventoryActionTypeName getType() {
        return InventoryActionType.InventoryActionTypeName.SHIPMENT;
    }

    @Override
    public InventoryAction doAction(InventoryActionRequestDto requestDto) {
        InventoryAction inventoryAction = inventoryActionMapper.toModelWithQuantity(requestDto);
        checkAndUpdateInventory(requestDto);
        setAndUpdateWarehouse(inventoryAction, requestDto);
        Product product = productRepositoryService.getById(
                requestDto.getProductId());
        inventoryAction.setProduct(product);
        inventoryAction.setCreatedAt(LocalDateTime.now());
        setInventoryActionType(inventoryAction);
        setPrices(inventoryAction, product);
        return inventoryActionRepositoryService.save(inventoryAction);
    }

    private void checkAndUpdateInventory(InventoryActionRequestDto requestDto) {
        Inventory inventory = inventoryRepositoryService.getByProductIdAndWarehouseId(
                requestDto.getProductId(), requestDto.getWarehouseId());
        checkInventoryQuantity(inventory, requestDto.getQuantity());
        inventory.setQuantity(inventory.getQuantity() - requestDto.getQuantity());
        inventoryRepositoryService.save(inventory);
    }

    private void checkInventoryQuantity(Inventory inventory, Long quantity) {
        if (inventory.getQuantity() < quantity) {
            throw new InventoryQuantityException(
                    "Cant ship product with id: " + inventory.getProduct().getId()
                            + " from warehouse with id: " + inventory.getWarehouse().getId()
                            + " with quantity: " + quantity
                            + " because actual quantity: " + inventory.getQuantity());
        }
    }

    private void setAndUpdateWarehouse(
            InventoryAction inventoryAction, InventoryActionRequestDto requestDto) {
        Warehouse warehouse = warehouseRepositoryService.getById(requestDto.getWarehouseId());
        warehouse.setFreeCapacity(warehouse.getFreeCapacity() + requestDto.getQuantity());
        warehouseRepositoryService.save(warehouse);
        inventoryAction.setWarehouse(warehouse);
    }

    private void setInventoryActionType(InventoryAction inventoryAction) {
        InventoryActionType actionType = inventoryActionTypeRepositoryService
                .getByTypeName(InventoryActionType.InventoryActionTypeName.SHIPMENT);
        inventoryAction.setInventoryActionType(actionType);
    }

    private void setPrices(InventoryAction inventoryAction, Product product) {
        inventoryAction.setRetailPrice(product.getRetailPrice());
        inventoryAction.setWholesalePrice(product.getWholesalePrice());
    }
}
