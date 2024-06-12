package project.inventorymanager.testutil;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SqlScriptPath {
    DELETE_DATA("database/deletes/delete-all-data.sql"),
    INSERT_CATEGORIES("database/insert/categories/insert-three-categories.sql"),
    INSERT_WAREHOUSES("database/insert/warehouses/insert-two-warehouses.sql"),
    INSERT_PRODUCTS("database/insert/products/insert-three-products.sql"),
    INSERT_INVENTORIES("database/insert/inventories/inset-inventories.sql"),
    INSERT_USER("database/insert/users/insert-three-users.sql"),
    INSERT_INVENTORY_ACTIONS("database/insert/inventoryactions/insert-five-inventory-actions.sql"),
    INSERT_STATISTIC_FILE("database/insert/statisticfiles/insert-three-statistic-file.sql");

    private final String path;
}
