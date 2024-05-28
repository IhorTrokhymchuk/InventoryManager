package project.inventorymanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import project.inventorymanager.config.MapperConfig;
import project.inventorymanager.dto.inventoryaction.excel.InventoryActionExcelDto;
import project.inventorymanager.dto.inventoryaction.request.InventoryActionRequestDto;
import project.inventorymanager.dto.inventoryaction.response.InventoryActionResponseDto;
import project.inventorymanager.model.inventoryaction.InventoryAction;

@Mapper(config = MapperConfig.class,
        uses = {ProductMapper.class, WarehouseMapper.class, InventoryActionTypeMapper.class})
public interface InventoryActionMapper {
    InventoryAction toModelWithQuantity(InventoryActionRequestDto requestDto);

    @Mapping(target = "product", qualifiedByName = "toResponseDto")
    InventoryActionResponseDto toResponseDto(InventoryAction inventoryAction);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "warehouseId", source = "warehouse.id")
    @Mapping(target = "warehouseLocation", source = "warehouse.location")
    @Mapping(target = "actionTypeName", source = "inventoryActionType.name")
    InventoryActionExcelDto toExcelDto(InventoryAction inventoryAction);
}
