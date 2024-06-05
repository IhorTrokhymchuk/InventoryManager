package project.inventorymanager.dto.product.request;

import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;

@Data
public class ProductPatchDto {
    private String name;
    private BigDecimal wholesalePrice;
    private BigDecimal retailPrice;
    private Set<Long> categoryIds;
    private String description;
}
