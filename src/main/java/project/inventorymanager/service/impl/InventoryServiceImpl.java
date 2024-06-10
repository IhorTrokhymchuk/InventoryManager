package project.inventorymanager.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.inventorymanager.dto.inventory.response.InventoryResponseDto;
import project.inventorymanager.mapper.InventoryMapper;
import project.inventorymanager.repositoryservice.InventoryRepositoryService;
import project.inventorymanager.service.InventoryService;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepositoryService inventoryRepositoryService;
    private final InventoryMapper inventoryMapper;

    @Override
    public List<InventoryResponseDto> findAll(Pageable pageable) {
        return inventoryRepositoryService.findAll(pageable).stream()
                .map(inventoryMapper::toResponseDto)
                .toList();
    }

    @Override
    public InventoryResponseDto getById(Long id) {
        return inventoryMapper.toResponseDto(inventoryRepositoryService.getById(id));
    }
}
