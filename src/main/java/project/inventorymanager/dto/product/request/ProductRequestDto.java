package project.inventorymanager.dto.product.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;

@Data
public class ProductRequestDto {
    @NotNull
    private String name;
    @NotNull
    private String uniqCode;
    @NotNull
    @Positive
    private BigDecimal wholesalePrice;
    @NotNull
    @Positive
    private BigDecimal retailPrice;
    @NotNull
    private Set<Long> categoryIds;
    private String description;
}
