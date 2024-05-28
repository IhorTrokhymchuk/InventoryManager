package project.inventorymanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import project.inventorymanager.config.MapperConfig;
import project.inventorymanager.dto.product.request.ProductRequestDto;
import project.inventorymanager.dto.product.response.ProductResponseDto;
import project.inventorymanager.model.product.Product;

@Mapper(config = MapperConfig.class, uses = {CategoryMapper.class})
public interface ProductMapper {
    ProductResponseDto toResponseDto(Product product);

    @Mapping(target = "wholesalePrice", ignore = true)
    ProductResponseDto toResponseDtoWithoutWholesalePrice(Product product);

    Product toModelWithoutCategories(ProductRequestDto requestDto);

    void updateProduct(@MappingTarget Product product, ProductRequestDto requestDto);

}
