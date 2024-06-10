package project.inventorymanager.service.impl;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.inventorymanager.dto.inventoryaction.request.InventoryActionRequestDto;
import project.inventorymanager.dto.inventoryaction.response.InventoryActionResponseDto;
import project.inventorymanager.mapper.InventoryActionMapper;
import project.inventorymanager.model.inventoryaction.InventoryAction;
import project.inventorymanager.model.inventoryaction.InventoryActionType;
import project.inventorymanager.repositoryservice.InventoryActionRepositoryService;
import project.inventorymanager.repositoryservice.InventoryActionTypeRepositoryService;
import project.inventorymanager.service.InventoryActionService;
import project.inventorymanager.service.inventoryactionstrategy.InventoryActionStrategy;
import project.inventorymanager.service.inventoryactionstrategy.InventoryActionStrategyFactory;

@RequiredArgsConstructor
@Service
public class InventoryActionServiceImpl implements InventoryActionService {
    private final InventoryActionStrategyFactory inventoryActionStrategyFactory;
    private final InventoryActionTypeRepositoryService inventoryActionTypeRepositoryService;
    private final InventoryActionRepositoryService inventoryActionRepositoryService;
    private final InventoryActionMapper inventoryActionMapper;

    @Override
    @Transactional
    public InventoryActionResponseDto save(InventoryActionRequestDto requestDto) {
        InventoryActionType inventoryActionType = inventoryActionTypeRepositoryService
                .getById(requestDto.getInventoryActionTypeId());
        InventoryActionStrategy inventoryActionStrategy
                = inventoryActionStrategyFactory.getStrategy(inventoryActionType.getName());
        InventoryAction inventoryAction = inventoryActionStrategy.doAction(requestDto);
        return inventoryActionMapper.toResponseDto(inventoryAction);
    }

    @Override
    public InventoryActionResponseDto getById(Long id) {
        return inventoryActionMapper.toResponseDto(
                inventoryActionRepositoryService.getById(id));
    }

    @Override
    public List<InventoryActionResponseDto> findAll(Pageable pageable) {
        return inventoryActionRepositoryService.findAll(pageable).stream()
                .map(inventoryActionMapper::toResponseDto)
                .toList();
    }
}
