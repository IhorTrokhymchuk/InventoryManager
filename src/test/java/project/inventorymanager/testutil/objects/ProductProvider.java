package project.inventorymanager.testutil.objects;

import static project.inventorymanager.testutil.objects.CategoryProvider.getCategory;
import static project.inventorymanager.testutil.objects.CategoryProvider.getCategoryResponseDto;

import java.math.BigDecimal;
import java.util.Set;
import project.inventorymanager.dto.product.request.ProductPatchDto;
import project.inventorymanager.dto.product.request.ProductRequestDto;
import project.inventorymanager.dto.product.response.ProductResponseDto;
import project.inventorymanager.model.product.Product;

public class ProductProvider {
    private static final BigDecimal RETAIL_PRICE = BigDecimal.valueOf(100);
    private static final BigDecimal WHOLESALE_PRICE = BigDecimal.valueOf(90);
    private static final String NAME = "name";
    private static final String UNIQ_CODE = "uniqCode";

    private static final BigDecimal UPD_RETAIL_PRICE = BigDecimal.valueOf(80);
    private static final BigDecimal UPD_WHOLESALE_PRICE = BigDecimal.valueOf(60);
    private static final String UPD_NAME = "nameUpd";
    private static final String UPD_UNIQ_CODE = "uniqUpd";

    public static Product getProduct(Long id) {
        Product product = new Product();
        product.setId(id);
        product.setName(NAME + id);
        product.setUniqCode(UNIQ_CODE + id);
        product.setCategories(Set.of(getCategory(1L), getCategory(2L)));
        product.setRetailPrice(RETAIL_PRICE.add(BigDecimal.valueOf(id)));
        product.setWholesalePrice(WHOLESALE_PRICE.add(BigDecimal.valueOf(id)));
        return product;
    }

    public static ProductResponseDto getProductResponseDto(Long id) {
        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setId(id);
        productResponseDto.setName(NAME + id);
        productResponseDto.setUniqCode(UNIQ_CODE + id);
        productResponseDto.setCategories(Set.of(getCategoryResponseDto(1L),
                getCategoryResponseDto(2L)));
        productResponseDto.setRetailPrice(RETAIL_PRICE.add(BigDecimal.valueOf(id)));
        productResponseDto.setWholesalePrice(WHOLESALE_PRICE.add(BigDecimal.valueOf(id)));
        return productResponseDto;
    }

    public static ProductRequestDto getProductRequestDto(Long id) {
        ProductRequestDto productResponseDto = new ProductRequestDto();
        productResponseDto.setName(NAME + id);
        productResponseDto.setUniqCode(UNIQ_CODE + id);
        productResponseDto.setCategoryIds(Set.of(1L, 2L));
        productResponseDto.setRetailPrice(RETAIL_PRICE.add(BigDecimal.valueOf(id)));
        productResponseDto.setWholesalePrice(WHOLESALE_PRICE.add(BigDecimal.valueOf(id)));
        return productResponseDto;
    }

    public static ProductPatchDto getProductPatchDto(Long id) {
        ProductPatchDto productResponseDto = new ProductPatchDto();
        productResponseDto.setName(UPD_NAME + id);
        productResponseDto.setCategoryIds(Set.of(1L, 2L));
        productResponseDto.setRetailPrice(UPD_RETAIL_PRICE.add(BigDecimal.valueOf(id)));
        productResponseDto.setWholesalePrice(UPD_WHOLESALE_PRICE.add(BigDecimal.valueOf(id)));
        return productResponseDto;
    }

}
