package project.inventorymanager.dto.product.response;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ProductResponseDto {
    private Long id;
    private String name;
    private String uniqCode;
    private BigDecimal wholesalePrice;
    private BigDecimal retailPrice;
    private String description;
}
