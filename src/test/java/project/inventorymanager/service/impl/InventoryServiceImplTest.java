package project.inventorymanager.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static project.inventorymanager.testutil.objects.InventoryProvider.getInventory;
import static project.inventorymanager.testutil.objects.InventoryProvider.getInventoryResponseDto;

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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import project.inventorymanager.dto.inventory.response.InventoryResponseDto;
import project.inventorymanager.mapper.InventoryMapper;
import project.inventorymanager.model.inventory.Inventory;
import project.inventorymanager.repositoryservice.InventoryRepositoryService;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceImplTest {
    @Mock
    private InventoryRepositoryService inventoryRepositoryService;
    @Mock
    private InventoryMapper inventoryMapper;
    @InjectMocks
    private InventoryServiceImpl inventoryService;

    private Inventory inventory;
    private InventoryResponseDto responseDto;

    @BeforeEach
    void setup() {
        Long id = 1L;
        inventory = getInventory(id);
        responseDto = getInventoryResponseDto(id);
    }

    @Test
    @DisplayName("Find all inventory with valid data")
    void findAll_withValidPageable_inventoryItems() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Inventory> inventories = List.of(inventory);
        Page<Inventory> page = new PageImpl<>(inventories);

        when(inventoryRepositoryService.findAll(pageable)).thenReturn(page);
        when(inventoryMapper.toResponseDto(inventory)).thenReturn(responseDto);

        List<InventoryResponseDto> result = inventoryService.findAll(pageable);

        assertEquals(List.of(responseDto), result);
        verify(inventoryRepositoryService).findAll(pageable);
        verify(inventoryMapper, times(inventories.size())).toResponseDto(inventory);
        verifyNoMoreInteractions(inventoryRepositoryService, inventoryMapper);
    }

    @Test
    @DisplayName("Get inventory by valid id")
    void getById_withValidId_inventoryItem() {
        Long id = 1L;
        when(inventoryRepositoryService.getById(id)).thenReturn(inventory);
        when(inventoryMapper.toResponseDto(inventory)).thenReturn(responseDto);

        InventoryResponseDto result = inventoryService.getById(id);

        assertEquals(responseDto, result);
        verify(inventoryRepositoryService).getById(id);
        verify(inventoryMapper).toResponseDto(inventory);
        verifyNoMoreInteractions(inventoryRepositoryService, inventoryMapper);
    }
}
