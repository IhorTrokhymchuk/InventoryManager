package project.inventorymanager.service.inventoryactionstrategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static project.inventorymanager.testutil.objects.InventoryActionProvider.getInventoryAction;
import static project.inventorymanager.testutil.objects.InventoryActionProvider.getInventoryActionRequestDto;
import static project.inventorymanager.testutil.objects.InventoryProvider.getInventory;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

@ExtendWith(MockitoExtension.class)
public class ReplenishmentStrategyTest {

    @Mock
    private InventoryActionTypeRepositoryService inventoryActionTypeRepositoryService;
    @Mock
    private ProductRepositoryService productRepositoryService;
    @Mock
    private WarehouseRepositoryService warehouseRepositoryService;
    @Mock
    private InventoryActionRepositoryService inventoryActionRepositoryService;
    @Mock
    private InventoryRepositoryService inventoryRepositoryService;
    @Mock
    private InventoryActionMapper inventoryActionMapper;

    @InjectMocks
    private ReplenishmentStrategy replenishmentStrategy;

    private InventoryActionRequestDto requestDto;
    private Inventory inventory;
    private Product product;
    private Warehouse warehouse;
    private InventoryAction inventoryAction;
    private InventoryActionType actionType;

    @BeforeEach
    void setUp() {
        requestDto = getInventoryActionRequestDto(1L, 2L);
        inventory = getInventory(1L);
        inventoryAction = getInventoryAction(1L, 2L);
        product = inventoryAction.getProduct();
        warehouse = inventoryAction.getWarehouse();
        actionType = inventoryAction.getInventoryActionType();
    }

    @Test
    @DisplayName("Create and do inventory action with exist inventory")
    void doAction_createAndSaveInventoryActionWithExistInventory_inventoryAction() {
        when(inventoryActionMapper.toModelWithQuantity(any())).thenReturn(inventoryAction);
        when(productRepositoryService.getById(anyLong())).thenReturn(product);
        when(warehouseRepositoryService.getById(anyLong())).thenReturn(warehouse);
        when(inventoryRepositoryService.findByProductIdAndWarehouseId(anyLong(), anyLong()))
                .thenReturn(Optional.of(inventory));
        when(inventoryActionTypeRepositoryService.getByTypeName(any())).thenReturn(actionType);
        when(inventoryActionRepositoryService.save(any())).thenReturn(inventoryAction);

        InventoryAction result = replenishmentStrategy.doAction(requestDto);

        assertNotNull(result);
        assertEquals(product, result.getProduct());
        assertEquals(warehouse, result.getWarehouse());
        assertEquals(actionType, result.getInventoryActionType());
        assertEquals(inventoryAction.getRetailPrice(), result.getRetailPrice());
        assertEquals(inventoryAction.getWholesalePrice(), result.getWholesalePrice());

        verify(inventoryActionMapper).toModelWithQuantity(requestDto);
        verify(productRepositoryService).getById(1L);
        verify(warehouseRepositoryService).getById(1L);
        verify(inventoryRepositoryService).findByProductIdAndWarehouseId(1L, 1L);
        verify(inventoryActionTypeRepositoryService)
                .getByTypeName(InventoryActionType.InventoryActionTypeName.REPLENISHMENT);
        verify(inventoryActionRepositoryService).save(inventoryAction);
        verify(inventoryRepositoryService).save(inventory);
        verify(warehouseRepositoryService).save(warehouse);
    }

    @Test
    @DisplayName("Throw exception with non exit free capacity")
    void doAction_doActionWithNonValidData_inventoryAction() {
        warehouse.setFreeCapacity(5L);
        when(warehouseRepositoryService.getById(anyLong())).thenReturn(warehouse);

        WarehouseDontHaveFreeCapacityException exception = assertThrows(
                WarehouseDontHaveFreeCapacityException.class,
                () -> replenishmentStrategy.doAction(requestDto));

        assertEquals("Warehouse with id: 1 dont have capacity for 11 products with id: 1. "
                + "Available free capacity: 5", exception.getMessage());
    }

    @Test
    @DisplayName("Create and do inventory action with non exist inventory")
    void doAction_createAndSaveInventoryActionWithNonExistInventory_inventoryAction() {
        when(inventoryActionMapper.toModelWithQuantity(any())).thenReturn(inventoryAction);
        when(productRepositoryService.getById(anyLong())).thenReturn(product);
        when(warehouseRepositoryService.getById(anyLong())).thenReturn(warehouse);
        when(inventoryRepositoryService.findByProductIdAndWarehouseId(anyLong(), anyLong()))
                .thenReturn(Optional.empty());
        when(inventoryActionTypeRepositoryService.getByTypeName(any())).thenReturn(actionType);
        when(inventoryActionRepositoryService.save(any())).thenReturn(inventoryAction);

        InventoryAction result = replenishmentStrategy.doAction(requestDto);

        assertNotNull(result);
        assertEquals(product, result.getProduct());
        assertEquals(warehouse, result.getWarehouse());
        assertEquals(actionType, result.getInventoryActionType());
        assertEquals(inventoryAction.getRetailPrice(), result.getRetailPrice());
        assertEquals(inventoryAction.getWholesalePrice(), result.getWholesalePrice());

        verify(inventoryActionMapper).toModelWithQuantity(requestDto);
        verify(productRepositoryService).getById(1L);
        verify(warehouseRepositoryService).getById(1L);
        verify(inventoryRepositoryService).findByProductIdAndWarehouseId(1L, 1L);
        verify(inventoryActionTypeRepositoryService)
                .getByTypeName(InventoryActionType.InventoryActionTypeName.REPLENISHMENT);
        verify(inventoryActionRepositoryService).save(inventoryAction);
        verify(inventoryRepositoryService).save(any(Inventory.class));
    }
}
