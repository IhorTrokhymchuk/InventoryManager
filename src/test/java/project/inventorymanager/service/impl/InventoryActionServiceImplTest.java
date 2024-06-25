package project.inventorymanager.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static project.inventorymanager.testutil.objects.InventoryActionProvider.getInventoryAction;
import static project.inventorymanager.testutil.objects.InventoryActionProvider.getInventoryActionRequestDto;
import static project.inventorymanager.testutil.objects.InventoryActionProvider.getInventoryActionResponseDto;
import static project.inventorymanager.testutil.objects.InventoryActionTypeProvider.getInventoryActionTypeById;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import project.inventorymanager.dto.inventoryaction.request.InventoryActionRequestDto;
import project.inventorymanager.dto.inventoryaction.response.InventoryActionResponseDto;
import project.inventorymanager.mapper.InventoryActionMapper;
import project.inventorymanager.model.inventoryaction.InventoryAction;
import project.inventorymanager.model.inventoryaction.InventoryActionType;
import project.inventorymanager.repositoryservice.InventoryActionRepositoryService;
import project.inventorymanager.repositoryservice.InventoryActionTypeRepositoryService;
import project.inventorymanager.service.inventoryactionstrategy.InventoryActionStrategy;
import project.inventorymanager.service.inventoryactionstrategy.InventoryActionStrategyFactory;

@ExtendWith(MockitoExtension.class)
public class InventoryActionServiceImplTest {

    @Mock
    private InventoryActionStrategyFactory inventoryActionStrategyFactory;

    @Mock
    private InventoryActionTypeRepositoryService inventoryActionTypeRepositoryService;

    @Mock
    private InventoryActionRepositoryService inventoryActionRepositoryService;

    @Mock
    private InventoryActionMapper inventoryActionMapper;

    @InjectMocks
    private InventoryActionServiceImpl inventoryActionService;

    private InventoryActionRequestDto requestDto;
    private InventoryAction inventoryAction;
    private InventoryActionResponseDto responseDto;
    private InventoryActionType inventoryActionType;
    private InventoryActionStrategy inventoryActionStrategy;

    @BeforeEach
    void setup() {
        requestDto = getInventoryActionRequestDto(1L, 1L);
        requestDto.setInventoryActionTypeId(1L);

        inventoryAction = getInventoryAction(1L, 1L);

        responseDto = getInventoryActionResponseDto(1L, 1L);

        inventoryActionType = getInventoryActionTypeById(1L);

        inventoryActionStrategy = mock(InventoryActionStrategy.class);
    }

    @Test
    @DisplayName("Save Inventory Action with valid data")
    void saveInventoryAction_withValidData() {
        when(inventoryActionTypeRepositoryService.getById(requestDto.getInventoryActionTypeId()))
                .thenReturn(inventoryActionType);
        when(inventoryActionStrategyFactory.getStrategy(inventoryActionType.getName()))
                .thenReturn(inventoryActionStrategy);
        when(inventoryActionStrategy.doAction(requestDto)).thenReturn(inventoryAction);
        when(inventoryActionMapper.toResponseDto(inventoryAction)).thenReturn(responseDto);

        InventoryActionResponseDto result = inventoryActionService.save(requestDto);

        assertEquals(responseDto, result);
        verify(inventoryActionTypeRepositoryService).getById(requestDto.getInventoryActionTypeId());
        verify(inventoryActionStrategyFactory).getStrategy(inventoryActionType.getName());
        verify(inventoryActionStrategy).doAction(requestDto);
        verify(inventoryActionMapper).toResponseDto(inventoryAction);
    }

    @Test
    @DisplayName("Get Inventory Action by id with valid data")
    void getById_withValidData_inventoryAction() {
        Long id = 1L;
        when(inventoryActionRepositoryService.getById(id)).thenReturn(inventoryAction);
        when(inventoryActionMapper.toResponseDto(inventoryAction)).thenReturn(responseDto);

        InventoryActionResponseDto result = inventoryActionService.getById(id);

        assertEquals(responseDto, result);
        verify(inventoryActionRepositoryService).getById(id);
        verify(inventoryActionMapper).toResponseDto(inventoryAction);
    }

    @Test
    @DisplayName("Find all Inventory Actions with valid pageable")
    void findAll_withValidPageable_inventoryActions() {
        Pageable pageable = mock(Pageable.class);
        List<InventoryAction> inventoryActions = List.of(inventoryAction);
        Page<InventoryAction> page = new PageImpl<>(inventoryActions);
        when(inventoryActionRepositoryService.findAll(pageable)).thenReturn(page);
        when(inventoryActionMapper.toResponseDto(inventoryAction)).thenReturn(responseDto);

        List<InventoryActionResponseDto> result = inventoryActionService.findAll(pageable);

        assertEquals(List.of(responseDto), result);
        verify(inventoryActionRepositoryService).findAll(pageable);
        verify(inventoryActionMapper, times(inventoryActions.size()))
                .toResponseDto(inventoryAction);
    }
}
