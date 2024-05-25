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
public class ShipmentStrategy implements InventoryActionStrategy {
    private final InventoryActionTypeRepoService inventoryActionTypeRepoService;
    private final UserRepoService userRepoService;
    private final ProductRepoService productRepoService;
    private final WarehouseRepoService warehouseRepoService;
    private final InventoryActionRepoService inventoryActionRepoService;
    private final InventoryRepoService inventoryRepoService;
    private final InventoryActionMapper inventoryActionMapper;

    @Override
    public InventoryActionType.InventoryActionTypeName getType() {
        return InventoryActionType.InventoryActionTypeName.SHIPMENT;
    }

    @Override
    public InventoryAction doAction(InventoryActionRequestDto requestDto, String email) {
        InventoryAction inventoryAction = inventoryActionMapper.toModelWithQuantity(requestDto);
        checkAndUpdateInventory(requestDto, email);
        setAndUpdateWarehouse(inventoryAction, requestDto, email);
        User user = userRepoService.getByEmail(email);
        inventoryAction.setUser(user);
        Product product = productRepoService.getByIdIfUserHavePermission(
                requestDto.getProductId(), email);
        inventoryAction.setProduct(product);
        inventoryAction.setCreatedAt(LocalDateTime.now());
        setInventoryActionType(inventoryAction);
        setPrices(inventoryAction, product);
        return inventoryActionRepoService.save(inventoryAction);
    }

    private void checkAndUpdateInventory(InventoryActionRequestDto requestDto, String email) {
        Inventory inventory = inventoryRepoService.getByProductIdAndWarehouseIdAndUserEmail(
                requestDto.getProductId(), requestDto.getWarehouseId(), email);
        checkInventoryQuantity(inventory, requestDto.getQuantity());
        inventory.setQuantity(inventory.getQuantity() - requestDto.getQuantity());
        inventoryRepoService.save(inventory);
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
            InventoryAction inventoryAction, InventoryActionRequestDto requestDto, String email) {
        Warehouse warehouse = warehouseRepoService
                .getByIdIfUserHavePermission(requestDto.getWarehouseId(), email);
        warehouse.setFreeCapacity(warehouse.getFreeCapacity() + requestDto.getQuantity());
        warehouseRepoService.save(warehouse);
        inventoryAction.setWarehouse(warehouse);
    }

    private void setInventoryActionType(InventoryAction inventoryAction) {
        InventoryActionType actionType = inventoryActionTypeRepoService
                .getByTypeName(InventoryActionType.InventoryActionTypeName.SHIPMENT);
        inventoryAction.setInventoryActionType(actionType);
    }

    private void setPrices(InventoryAction inventoryAction, Product product) {
        inventoryAction.setRetailPrice(product.getRetailPrice());
        inventoryAction.setWholesalePrice(product.getWholesalePrice());
    }
}
