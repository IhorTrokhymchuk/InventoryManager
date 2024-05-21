package project.inventorymanager.dto.warehouse.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class WarehouseRequestDto {
    @NotNull
    private String location;
    @NotNull
    @Positive
    private Long capacity;
}
