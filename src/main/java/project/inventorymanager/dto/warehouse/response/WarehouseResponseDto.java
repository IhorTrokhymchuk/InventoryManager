package project.inventorymanager.dto.warehouse.response;

import lombok.Data;

@Data
public class WarehouseResponseDto {
    private Long id;
    private String location;
    private Long capacity;
    private Long freeCapacity;
}
