package project.inventorymanager.mapper;

import org.mapstruct.Mapper;
import project.inventorymanager.config.MapperConfig;
import project.inventorymanager.dto.warehouse.request.WarehouseRequestDto;
import project.inventorymanager.dto.warehouse.response.WarehouseResponseDto;
import project.inventorymanager.model.warehouse.Warehouse;

@Mapper(config = MapperConfig.class)
public interface WarehouseMapper {
    WarehouseResponseDto toResponseDto(Warehouse model);

    Warehouse toModel(WarehouseRequestDto requestDto);
}
