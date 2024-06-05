package project.inventorymanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import project.inventorymanager.config.MapperConfig;
import project.inventorymanager.dto.product.request.ProductPatchDto;
import project.inventorymanager.dto.product.request.ProductRequestDto;
import project.inventorymanager.dto.product.response.ProductResponseDto;
import project.inventorymanager.model.product.Product;

@Mapper(config = MapperConfig.class, uses = {CategoryMapper.class})
public interface ProductMapper {

    @Named("toResponseDto")
    ProductResponseDto toResponseDto(Product product);

    @Mapping(target = "wholesalePrice", ignore = true)
    ProductResponseDto toResponseDtoWithoutWholesalePrice(Product product);

    Product toModelWithoutCategories(ProductRequestDto requestDto);

    void setParametersWithoutCategories(
            @MappingTarget Product product, ProductRequestDto requestDto);

    void setParametersWithoutCategories(
            @MappingTarget Product product, ProductPatchDto requestDto);
}
