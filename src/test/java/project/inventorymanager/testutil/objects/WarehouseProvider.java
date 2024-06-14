package project.inventorymanager.testutil.objects;

import org.springframework.stereotype.Component;
import project.inventorymanager.dto.warehouse.request.WarehouseRequestDto;
import project.inventorymanager.dto.warehouse.response.WarehouseResponseDto;
import project.inventorymanager.model.warehouse.Warehouse;

@Component
public class WarehouseProvider {
    public static final Long CAPACITY = 100L;
    private static final String LOCATION = "example";

    public static Warehouse getWarehouse(Long id) {
        Warehouse warehouse = new Warehouse();
        warehouse.setId(id);
        warehouse.setLocation(LOCATION + id);
        warehouse.setCapacity(CAPACITY);
        warehouse.setFreeCapacity(CAPACITY);
        return warehouse;
    }

    public static WarehouseResponseDto getWarehouseResponseDto(Long id) {
        WarehouseResponseDto warehouseResponseDto = new WarehouseResponseDto();
        warehouseResponseDto.setId(id);
        warehouseResponseDto.setLocation(LOCATION + id);
        warehouseResponseDto.setCapacity(CAPACITY);
        warehouseResponseDto.setFreeCapacity(CAPACITY);
        return warehouseResponseDto;
    }

    public static WarehouseRequestDto getWarehouseRequestDto(Long id) {
        WarehouseRequestDto warehouseRequestDto = new WarehouseRequestDto();
        warehouseRequestDto.setLocation(LOCATION + id);
        warehouseRequestDto.setCapacity(CAPACITY);
        return warehouseRequestDto;
    }
}
