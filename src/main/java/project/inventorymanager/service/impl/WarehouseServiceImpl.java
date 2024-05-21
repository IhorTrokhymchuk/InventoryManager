package project.inventorymanager.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.inventorymanager.dto.warehouse.request.WarehouseRequestDto;
import project.inventorymanager.dto.warehouse.response.WarehouseResponseDto;
import project.inventorymanager.exception.repository.EntityNotFoundException;
import project.inventorymanager.mapper.WarehouseMapper;
import project.inventorymanager.model.warehouse.Warehouse;
import project.inventorymanager.repository.WarehouseRepository;
import project.inventorymanager.service.WarehouseService;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;

    @Override
    public WarehouseResponseDto save(WarehouseRequestDto requestDto) {
        Warehouse warehouse = warehouseMapper.toModel(requestDto);
        Warehouse savedWarehouse = warehouseRepository.save(warehouse);
        return warehouseMapper.toResponseDto(savedWarehouse);
    }

    @Override
    public WarehouseResponseDto getById(Long id) {
        return warehouseMapper.toResponseDto(getWarehouseById(id));
    }

    @Override
    public List<WarehouseResponseDto> findAll(Pageable pageable) {
        return warehouseRepository.findAll(pageable).stream()
                .map(warehouseMapper::toResponseDto)
                .toList();
    }

    private Warehouse getWarehouseById(Long id) {
        return warehouseRepository.findById(id).orElseThrow(
                        () -> new EntityNotFoundException("Cant find warehouse with id: " + id));
    }
}
