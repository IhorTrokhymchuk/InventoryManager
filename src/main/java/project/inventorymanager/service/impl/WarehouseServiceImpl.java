package project.inventorymanager.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.inventorymanager.dto.warehouse.request.WarehouseRequestDto;
import project.inventorymanager.dto.warehouse.response.WarehouseResponseDto;
import project.inventorymanager.mapper.WarehouseMapper;
import project.inventorymanager.model.warehouse.Warehouse;
import project.inventorymanager.repositoryservice.WarehouseRepoService;
import project.inventorymanager.service.WarehouseService;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepoService warehouseRepoService;
    private final WarehouseMapper warehouseMapper;

    @Override
    public WarehouseResponseDto save(WarehouseRequestDto requestDto) {
        Warehouse warehouse = warehouseMapper.toModelWithoutUserAndFreeCapacity(requestDto);
        warehouse.setFreeCapacity(requestDto.getCapacity());
        return warehouseMapper.toResponseDto(warehouseRepoService.save(warehouse));
    }

    @Override
    public WarehouseResponseDto getById(Long id) {
        return warehouseMapper.toResponseDto(
                warehouseRepoService.getById(id));
    }

    @Override
    public List<WarehouseResponseDto> findAll(Pageable pageable) {
        return warehouseRepoService.findAll(pageable).stream()
                .map(warehouseMapper::toResponseDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        warehouseRepoService.deleteById(id);
    }
}
