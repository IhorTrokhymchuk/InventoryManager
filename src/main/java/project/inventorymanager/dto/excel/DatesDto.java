package project.inventorymanager.dto.excel;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;

@Data
public class DatesDto {
    @NotNull
    private LocalDate fromDate;
    @NotNull
    private LocalDate toDate;
}
