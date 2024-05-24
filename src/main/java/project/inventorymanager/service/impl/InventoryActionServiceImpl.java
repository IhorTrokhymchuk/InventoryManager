package project.inventorymanager.service.impl;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.inventorymanager.dto.inventoryaction.request.InventoryActionRequestDto;
import project.inventorymanager.dto.inventoryaction.response.InventoryActionResponseDto;
import project.inventorymanager.exception.warehouse.WarehouseDontHaveFreeCapacityException;
import project.inventorymanager.mapper.InventoryActionMapper;
import project.inventorymanager.model.inventoryaction.InventoryAction;
import project.inventorymanager.model.inventoryaction.InventoryActionType;
import project.inventorymanager.model.warehouse.Warehouse;
import project.inventorymanager.repositoryservice.InventoryActionRepoService;
import project.inventorymanager.repositoryservice.InventoryActionTypeRepoService;
import project.inventorymanager.service.InventoryActionService;
import project.inventorymanager.service.strategy.InventoryActionStrategy;
import project.inventorymanager.service.strategy.InventoryActionStrategyFactory;

@RequiredArgsConstructor
@Service
public class InventoryActionServiceImpl implements InventoryActionService {
    private final InventoryActionStrategyFactory inventoryActionStrategyFactory;
    private final InventoryActionTypeRepoService inventoryActionTypeRepoService;
    private final InventoryActionRepoService inventoryActionRepoService;
    private final InventoryActionMapper inventoryActionMapper;

    //todo: Logic for minus or plus inventory
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
    public InventoryActionResponseDto getById(Long id, String email) {
        return inventoryActionMapper.toResponseDto(
                inventoryActionRepoService.getByIdIfUserHavePermission(id, email));
    }

    @Override
    public List<InventoryActionResponseDto> findAll(Pageable pageable, String email) {
        return inventoryActionRepoService.findAllByUserEmail(pageable, email).stream()
                .map(inventoryActionMapper::toResponseDto)
                .toList();
    }

    private void isWarehouseIsFree(
            Warehouse warehouse,
            Long quantity) {
        if (warehouse.getFreeCapacity() < quantity) {
            throw new WarehouseDontHaveFreeCapacityException(
                    "Warehouse with id: " + warehouse.getId() + " dont have free capacity");
        }
    }
}
