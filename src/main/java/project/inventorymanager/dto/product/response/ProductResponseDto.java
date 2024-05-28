package project.inventorymanager.dto.product.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;
import project.inventorymanager.dto.category.response.CategoryResponseDto;

@Data
public class ProductResponseDto {
    private Long id;
    private String name;
    private String uniqCode;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal wholesalePrice;
    private BigDecimal retailPrice;
    private Set<CategoryResponseDto> categories;
    private String description;
}
