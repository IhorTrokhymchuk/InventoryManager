package project.inventorymanager.service.strategy;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import project.inventorymanager.model.inventoryaction.InventoryActionType;

@Component
public class InventoryActionStrategyFactory {
    private final Map<InventoryActionType.InventoryActionTypeName, InventoryActionStrategy>
            strategies;

    @Autowired
    public InventoryActionStrategyFactory(List<InventoryActionStrategy> strategyList) {
        strategies = strategyList.stream().collect(Collectors.toMap(
                InventoryActionStrategy::getType,
                strategy -> strategy
        ));
    }

    public InventoryActionStrategy getStrategy(
            InventoryActionType.InventoryActionTypeName actionTypeName) {
        InventoryActionStrategy strategy = strategies.get(actionTypeName);
        if (strategy == null) {
            throw new IllegalArgumentException(
                    "No strategy found for action type: " + actionTypeName);
        }
        return strategy;
    }
}
