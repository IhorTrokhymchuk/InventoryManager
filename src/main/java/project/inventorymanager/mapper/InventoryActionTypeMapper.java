package project.inventorymanager.mapper;

import org.mapstruct.Mapper;
import project.inventorymanager.config.MapperConfig;
import project.inventorymanager.dto.inventoryaction.response.InventoryActionResponseDto;
import project.inventorymanager.model.inventoryaction.InventoryActionType;

@Mapper(config = MapperConfig.class)
public interface InventoryActionTypeMapper {
    InventoryActionResponseDto toResponseDto(InventoryActionType inventoryActionType);
}
