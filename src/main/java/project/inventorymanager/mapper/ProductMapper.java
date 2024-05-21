package project.inventorymanager.mapper;

import org.mapstruct.Mapper;
import project.inventorymanager.config.MapperConfig;
import project.inventorymanager.dto.product.request.ProductRequestDto;
import project.inventorymanager.dto.product.response.ProductResponseDto;
import project.inventorymanager.model.product.Product;

@Mapper(config = MapperConfig.class)
public interface ProductMapper {
    ProductResponseDto toResponseDto(Product product);

    Product toModelWithoutUser(ProductRequestDto requestDto);
}
