package project.inventorymanager.service.inventoryactionstrategy;

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
import project.inventorymanager.model.user.User;
import project.inventorymanager.model.warehouse.Warehouse;
import project.inventorymanager.repositoryservice.InventoryActionRepoService;
import project.inventorymanager.repositoryservice.InventoryActionTypeRepoService;
import project.inventorymanager.repositoryservice.InventoryRepoService;
import project.inventorymanager.repositoryservice.ProductRepoService;
import project.inventorymanager.repositoryservice.UserRepoService;
import project.inventorymanager.repositoryservice.WarehouseRepoService;

@Component
@RequiredArgsConstructor
public class ReplenishmentStrategy implements InventoryActionStrategy {
    private final InventoryActionTypeRepoService inventoryActionTypeRepoService;
    private final UserRepoService userRepoService;
    private final ProductRepoService productRepoService;
    private final WarehouseRepoService warehouseRepoService;
    private final InventoryActionRepoService inventoryActionRepoService;
    private final InventoryRepoService inventoryRepoService;
    private final InventoryActionMapper inventoryActionMapper;

    @Override
    public InventoryActionType.InventoryActionTypeName getType() {
        return InventoryActionType.InventoryActionTypeName.REPLENISHMENT;
    }

    @Override
    public InventoryAction doAction(InventoryActionRequestDto requestDto, String email) {
        InventoryAction inventoryAction = inventoryActionMapper.toModelWithQuantity(requestDto);
        Product product = productRepoService
                .getByIdIfUserHavePermission(requestDto.getProductId(), email);
        User user = userRepoService.getByEmail(email);
        Warehouse warehouse = warehouseRepoService
                .getByIdIfUserHavePermission(requestDto.getWarehouseId(), email);
        setAndUpdateWarehouse(inventoryAction, warehouse, requestDto);
        updateOrCreateInventory(product, warehouse, user, requestDto.getQuantity());
        inventoryAction.setUser(user);
        inventoryAction.setProduct(product);
        setInventoryActionType(inventoryAction);
        inventoryAction.setCreatedAt(LocalDateTime.now());
        inventoryActionRepoService.save(inventoryAction);
        return inventoryAction;
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
        warehouseRepoService.save(warehouse);
        inventoryAction.setWarehouse(warehouse);
    }

    private void updateOrCreateInventory(
            Product product, Warehouse warehouse, User user, Long quantity) {
        Optional<Inventory> optionalInventory
                = inventoryRepoService.findByProductIdAndWarehouseIdAndUserEmail(
                        product.getId(), warehouse.getId(), user.getEmail());
        Inventory inventory;
        if (optionalInventory.isPresent()) {
            inventory = optionalInventory.get();
            inventory.setQuantity(inventory.getQuantity() + quantity);
        } else {
            inventory = new Inventory();
            inventory.setProduct(product);
            inventory.setWarehouse(warehouse);
            inventory.setUser(user);
            inventory.setQuantity(quantity);
        }
        inventoryRepoService.save(inventory);
    }

    private void setInventoryActionType(InventoryAction inventoryAction) {
        InventoryActionType actionType = inventoryActionTypeRepoService
                .getByTypeName(InventoryActionType.InventoryActionTypeName.REPLENISHMENT);
        inventoryAction.setInventoryActionType(actionType);
    }
}
