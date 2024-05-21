package project.inventorymanager.mapper;

import org.mapstruct.Mapper;
import project.inventorymanager.config.MapperConfig;
import project.inventorymanager.dto.inventory.response.InventoryResponseDto;
import project.inventorymanager.model.inventory.Inventory;

@Mapper(config = MapperConfig.class)
public interface InventoryMapper {
    InventoryResponseDto toResponseDto(Inventory inventory);
}
