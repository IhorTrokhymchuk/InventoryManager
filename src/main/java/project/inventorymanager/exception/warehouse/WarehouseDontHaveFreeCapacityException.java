package project.inventorymanager.exception.warehouse;

public class WarehouseDontHaveFreeCapacityException extends RuntimeException {
    public WarehouseDontHaveFreeCapacityException(String message) {
        super(message);
    }
}
