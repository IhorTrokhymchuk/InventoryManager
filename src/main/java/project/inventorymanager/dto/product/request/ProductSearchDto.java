package project.inventorymanager.dto.product.request;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ProductSearchDto {
    private String name;
    private String uniqCode;
    private BigDecimal wholesalePriceMin;
    private BigDecimal wholesalePriceMax;
    private BigDecimal retailPriceMin;
    private BigDecimal retailPriceMax;
    private Long[] categoryIds;
}
