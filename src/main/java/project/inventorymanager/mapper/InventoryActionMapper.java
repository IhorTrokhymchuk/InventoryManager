package project.inventorymanager.mapper;

import org.mapstruct.Mapper;
import project.inventorymanager.config.MapperConfig;
import project.inventorymanager.dto.inventoryaction.request.InventoryActionRequestDto;
import project.inventorymanager.dto.inventoryaction.response.InventoryActionResponseDto;
import project.inventorymanager.model.inventoryaction.InventoryAction;

@Mapper(config = MapperConfig.class,
        uses = {ProductMapper.class, WarehouseMapper.class, InventoryActionTypeMapper.class})
public interface InventoryActionMapper {
    InventoryAction toModelWithQuantity(InventoryActionRequestDto requestDto);

    InventoryActionResponseDto toResponseDto(InventoryAction inventoryAction);
}
