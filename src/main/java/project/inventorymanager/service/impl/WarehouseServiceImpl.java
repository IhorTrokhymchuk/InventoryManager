package project.inventorymanager.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.inventorymanager.dto.warehouse.request.WarehouseRequestDto;
import project.inventorymanager.dto.warehouse.response.WarehouseResponseDto;
import project.inventorymanager.mapper.WarehouseMapper;
import project.inventorymanager.model.warehouse.Warehouse;
import project.inventorymanager.repositoryservice.WarehouseRepositoryService;
import project.inventorymanager.service.WarehouseService;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepositoryService warehouseRepositoryService;
    private final WarehouseMapper warehouseMapper;

    @Override
    public WarehouseResponseDto save(WarehouseRequestDto requestDto) {
        Warehouse warehouse = warehouseMapper.toModelWithoutUserAndFreeCapacity(requestDto);
        warehouse.setFreeCapacity(requestDto.getCapacity());
        return warehouseMapper.toResponseDto(warehouseRepositoryService.save(warehouse));
    }

    @Override
    public WarehouseResponseDto getById(Long id) {
        return warehouseMapper.toResponseDto(
                warehouseRepositoryService.getById(id));
    }

    @Override
    public List<WarehouseResponseDto> findAll(Pageable pageable) {
        return warehouseRepositoryService.findAll(pageable).stream()
                .map(warehouseMapper::toResponseDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        warehouseRepositoryService.deleteById(id);
    }
}
