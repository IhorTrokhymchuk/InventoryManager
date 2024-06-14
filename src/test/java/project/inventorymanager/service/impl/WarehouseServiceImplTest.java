package project.inventorymanager.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static project.inventorymanager.testutil.objects.WarehouseProvider.getWarehouse;
import static project.inventorymanager.testutil.objects.WarehouseProvider.getWarehouseRequestDto;
import static project.inventorymanager.testutil.objects.WarehouseProvider.getWarehouseResponseDto;

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
import project.inventorymanager.dto.warehouse.request.WarehouseRequestDto;
import project.inventorymanager.dto.warehouse.response.WarehouseResponseDto;
import project.inventorymanager.mapper.WarehouseMapper;
import project.inventorymanager.model.warehouse.Warehouse;
import project.inventorymanager.repositoryservice.WarehouseRepositoryService;

@ExtendWith(MockitoExtension.class)
public class WarehouseServiceImplTest {
    @Mock
    private WarehouseRepositoryService warehouseRepositoryService;
    @Mock
    private WarehouseMapper warehouseMapper;
    @InjectMocks
    private WarehouseServiceImpl warehouseService;

    private WarehouseRequestDto requestDto;
    private Warehouse warehouse;
    private WarehouseResponseDto responseDto;

    @BeforeEach
    void setup() {
        Long id = 1L;
        requestDto = getWarehouseRequestDto(id);
        warehouse = getWarehouse(id);
        responseDto = getWarehouseResponseDto(id);
    }

    @Test
    @DisplayName("Save warehouse with valid data")
    void save_withValidData_savedWarehouse() {
        when(warehouseMapper.toModelWithoutUserAndFreeCapacity(requestDto)).thenReturn(warehouse);
        when(warehouseRepositoryService.save(warehouse)).thenReturn(warehouse);
        when(warehouseMapper.toResponseDto(warehouse)).thenReturn(responseDto);

        WarehouseResponseDto result = warehouseService.save(requestDto);

        assertNotNull(result);
        assertEquals(responseDto, result);
        verify(warehouseMapper, times(1)).toModelWithoutUserAndFreeCapacity(requestDto);
        verify(warehouseRepositoryService, times(1)).save(warehouse);
        verify(warehouseMapper, times(1)).toResponseDto(warehouse);
        verifyNoMoreInteractions(warehouseRepositoryService, warehouseMapper);
    }

    @Test
    @DisplayName("Get by id warehouse with valid data")
    void getById_withValidData_warehouse() {
        Long id = 1L;
        when(warehouseRepositoryService.getById(id)).thenReturn(warehouse);
        when(warehouseMapper.toResponseDto(warehouse)).thenReturn(responseDto);

        WarehouseResponseDto result = warehouseService.getById(id);

        assertNotNull(result);
        assertEquals(responseDto, result);
        verify(warehouseRepositoryService, times(1)).getById(id);
        verify(warehouseMapper, times(1)).toResponseDto(warehouse);
        verifyNoMoreInteractions(warehouseRepositoryService, warehouseMapper);
    }

    @Test
    @DisplayName("Find all warehouses with valid data")
    void findAll_withExistData_warehousesList() {
        Pageable pageable = mock(Pageable.class);
        List<Warehouse> warehouses = List.of(warehouse);
        Page<Warehouse> page = new PageImpl<>(warehouses);
        when(warehouseRepositoryService.findAll(pageable)).thenReturn(page);
        when(warehouseMapper.toResponseDto(warehouse)).thenReturn(responseDto);

        List<WarehouseResponseDto> result = warehouseService.findAll(pageable);

        assertEquals(List.of(responseDto), result);
        verify(warehouseRepositoryService, times(1)).findAll(pageable);
        verify(warehouseMapper, times(warehouses.size())).toResponseDto(warehouse);
        verifyNoMoreInteractions(warehouseRepositoryService, warehouseMapper);
    }

    @Test
    @DisplayName("Delete by id with valid data")
    void deleteById_withWalidData_delete() {
        Long id = 1L;

        warehouseService.deleteById(id);

        verify(warehouseRepositoryService, times(1)).deleteById(id);
        verifyNoMoreInteractions(warehouseRepositoryService, warehouseMapper);
    }
}
