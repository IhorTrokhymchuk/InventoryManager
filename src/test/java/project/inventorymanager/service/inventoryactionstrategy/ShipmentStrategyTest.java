package project.inventorymanager.service.inventoryactionstrategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static project.inventorymanager.testutil.objects.InventoryActionProvider.getInventoryAction;
import static project.inventorymanager.testutil.objects.InventoryActionProvider.getInventoryActionRequestDto;
import static project.inventorymanager.testutil.objects.InventoryProvider.getInventory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

@ExtendWith(MockitoExtension.class)
public class ShipmentStrategyTest {
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
    private ShipmentStrategy shipmentStrategy;

    private InventoryActionRequestDto requestDto;
    private Inventory inventory;
    private Product product;
    private Warehouse warehouse;
    private InventoryAction inventoryAction;
    private InventoryActionType actionType;

    @BeforeEach
    void setUp() {
        requestDto = getInventoryActionRequestDto(1L, 1L);
        inventory = getInventory(1L);
        inventoryAction = getInventoryAction(1L, 1L);
        product = inventoryAction.getProduct();
        warehouse = inventoryAction.getWarehouse();
        actionType = inventoryAction.getInventoryActionType();
    }

    @Test
    @DisplayName("Do action with valid data")
    void doAction_doActionWithValidData_inventoryAction() {
        when(inventoryActionMapper.toModelWithQuantity(any())).thenReturn(inventoryAction);
        when(inventoryRepositoryService.getByProductIdAndWarehouseId(anyLong(), anyLong()))
                .thenReturn(inventory);
        when(productRepositoryService.getById(anyLong())).thenReturn(product);
        when(inventoryActionTypeRepositoryService.getByTypeName(any())).thenReturn(actionType);
        when(inventoryActionRepositoryService.save(any())).thenReturn(inventoryAction);
        when(warehouseRepositoryService.getById(anyLong())).thenReturn(warehouse);

        InventoryAction result = shipmentStrategy.doAction(requestDto);

        assertNotNull(result);
        assertEquals(product, result.getProduct());
        assertEquals(warehouse, result.getWarehouse());
        assertEquals(actionType, result.getInventoryActionType());
        assertEquals(inventoryAction.getRetailPrice(), result.getRetailPrice());
        assertEquals(inventoryAction.getWholesalePrice(), result.getWholesalePrice());

        verify(inventoryActionMapper).toModelWithQuantity(requestDto);
        verify(inventoryRepositoryService).getByProductIdAndWarehouseId(1L, 1L);
        verify(productRepositoryService).getById(1L);
        verify(inventoryActionTypeRepositoryService)
                .getByTypeName(InventoryActionType.InventoryActionTypeName.SHIPMENT);
        verify(inventoryActionRepositoryService).save(inventoryAction);
        verify(warehouseRepositoryService).getById(1L);
        verify(inventoryRepositoryService).save(inventory);
        verify(warehouseRepositoryService).save(warehouse);
    }

    @Test
    @DisplayName("Throw exception with not valid quantity")
    void doAction_doActionWithNonValidQuantity_throwException() {
        inventory.setQuantity(5L);
        when(inventoryRepositoryService.getByProductIdAndWarehouseId(anyLong(), anyLong()))
                .thenReturn(inventory);

        InventoryQuantityException exception = assertThrows(InventoryQuantityException.class,
                () -> shipmentStrategy.doAction(requestDto));

        assertEquals("Cant ship product with id: 1 "
                + "from warehouse with id: 1 "
                + "with quantity: 11 because actual quantity: 5", exception.getMessage());
    }
}
