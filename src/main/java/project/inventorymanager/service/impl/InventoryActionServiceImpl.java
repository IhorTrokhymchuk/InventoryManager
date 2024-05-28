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
import project.inventorymanager.repositoryservice.InventoryActionRepoService;
import project.inventorymanager.repositoryservice.InventoryActionTypeRepoService;
import project.inventorymanager.service.InventoryActionService;
import project.inventorymanager.service.inventoryactionstrategy.InventoryActionStrategy;
import project.inventorymanager.service.inventoryactionstrategy.InventoryActionStrategyFactory;

@RequiredArgsConstructor
@Service
public class InventoryActionServiceImpl implements InventoryActionService {
    private final InventoryActionStrategyFactory inventoryActionStrategyFactory;
    private final InventoryActionTypeRepoService inventoryActionTypeRepoService;
    private final InventoryActionRepoService inventoryActionRepoService;
    private final InventoryActionMapper inventoryActionMapper;

    @Override
    @Transactional
    public InventoryActionResponseDto save(InventoryActionRequestDto requestDto, String email) {
        InventoryActionType inventoryActionType
                = inventoryActionTypeRepoService.getById(requestDto.getInventoryActionTypeId());
        InventoryActionStrategy inventoryActionStrategy
                = inventoryActionStrategyFactory.getStrategy(inventoryActionType.getName());
        InventoryAction inventoryAction = inventoryActionStrategy.doAction(requestDto, email);
        return inventoryActionMapper.toResponseDto(inventoryAction);
    }

    @Override
    @Transactional
    public InventoryActionResponseDto getById(Long id, String email) {
        return inventoryActionMapper.toResponseDto(
                inventoryActionRepoService.getByIdIfUserHavePermission(id, email));
    }

    @Override
    @Transactional
    public List<InventoryActionResponseDto> findAll(Pageable pageable, String email) {
        return inventoryActionRepoService.findAllByUserEmail(pageable, email).stream()
                .map(inventoryActionMapper::toResponseDto)
                .toList();
    }
}
