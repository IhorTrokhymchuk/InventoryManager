package project.inventorymanager.dto.category.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryRequestDto {
    @NotNull
    private String name;
    private String description;
}
