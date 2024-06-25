package project.inventorymanager.service.inventoryactionstrategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import project.inventorymanager.model.inventoryaction.InventoryActionType.InventoryActionTypeName;

class InventoryActionStrategyFactoryTest {

    @Mock
    private ShipmentStrategy shipmentStrategy;
    @Mock
    private ReplenishmentStrategy replenishmentStrategy;

    private InventoryActionStrategyFactory factory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(shipmentStrategy.getType()).thenReturn(InventoryActionTypeName.SHIPMENT);
        when(replenishmentStrategy.getType()).thenReturn(InventoryActionTypeName.REPLENISHMENT);

        List<InventoryActionStrategy> strategyList = Arrays.asList(
                shipmentStrategy, replenishmentStrategy);
        factory = new InventoryActionStrategyFactory(strategyList);
    }

    @Test
    @DisplayName("Get SHIPMENT strategy")
    void getStrategy_getShipmentStrategyWithValidData_shipmentStrategy() {
        InventoryActionStrategy strategy = factory.getStrategy(InventoryActionTypeName.SHIPMENT);
        assertNotNull(strategy);
        assertEquals(shipmentStrategy, strategy);
    }

    @Test
    @DisplayName("Get REPLENISHMENT strategy")
    void getStrategy_getReplenishmentStrategyWithValidData_replenishmentStrategy() {
        InventoryActionStrategy strategy = factory
                .getStrategy(InventoryActionTypeName.REPLENISHMENT);
        assertNotNull(strategy);
        assertEquals(replenishmentStrategy, strategy);
    }

    @Test
    @DisplayName("Get strategy with non valid type")
    void getStrategy_getStrategyWithNonValidData_throwException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factory.getStrategy(null);
        });

        assertEquals("No strategy found for action type: null", exception.getMessage());
    }
}
