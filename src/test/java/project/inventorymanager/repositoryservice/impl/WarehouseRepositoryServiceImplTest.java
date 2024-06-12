package project.inventorymanager.repositoryservice.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import project.inventorymanager.exception.repository.EntityNotFoundException;
import project.inventorymanager.model.warehouse.Warehouse;
import project.inventorymanager.repository.WarehouseRepository;

@ExtendWith(SpringExtension.class)
public class WarehouseRepositoryServiceImplTest {
    @Mock
    private WarehouseRepository warehouseRepository;
    @InjectMocks
    private WarehouseRepositoryServiceImpl warehouseRepositoryService;

    @Test
    @DisplayName("Save warehouse successfully")
    public void save_saveWarehouse_savedWarehouses() {
        Warehouse warehouse = new Warehouse();
        when(warehouseRepository.save(warehouse)).thenReturn(warehouse);

        Warehouse savedWarehouse = warehouseRepositoryService.save(warehouse);

        assertNotNull(savedWarehouse);
        verify(warehouseRepository, times(1)).save(warehouse);
    }

    @Test
    @DisplayName("Get warehouse by id successfully")
    public void getById_getWarehouseByExistId_warehouseOptional() {
        Long id = 1L;
        Warehouse warehouse = new Warehouse();
        when(warehouseRepository.findById(id)).thenReturn(Optional.of(warehouse));

        Warehouse foundWarehouse = warehouseRepositoryService.getById(id);

        assertNotNull(foundWarehouse);
        verify(warehouseRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Get warehouse by id throws EntityNotFoundException")
    public void getById_getByIdWithNonExistWarehouse_exception() {
        Long id = 1L;
        when(warehouseRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                warehouseRepositoryService.getById(id));

        assertEquals("Cant find warehouse with id: " + id, exception.getMessage());
        verify(warehouseRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Find all warehouses with pagination")
    public void findAll_findAllWithExistData_warehouseList() {
        Pageable pageable = mock(Pageable.class);
        Page<Warehouse> warehouses = mock(Page.class);
        when(warehouseRepository.findAll(pageable)).thenReturn(warehouses);

        Page<Warehouse> result = warehouseRepositoryService.findAll(pageable);

        assertNotNull(result);
        verify(warehouseRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Delete warehouse by id successfully")
    public void deleteById_deleteByIdWithExistData_delete() {
        Long id = 1L;
        when(warehouseRepository.existsById(id)).thenReturn(true);

        assertDoesNotThrow(() -> warehouseRepositoryService.deleteById(id));
        verify(warehouseRepository, times(1)).existsById(id);
        verify(warehouseRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Delete warehouse by id throws EntityNotFoundException")
    public void deleteById_deleteByIdWithNotExistData_exception() {
        Long id = 1L;
        when(warehouseRepository.existsById(id)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> warehouseRepositoryService.deleteById(id));

        assertEquals("Cant find warehouse with id: " + id, exception.getMessage());
        verify(warehouseRepository, times(1)).existsById(id);
        verify(warehouseRepository, times(0)).deleteById(id);
    }
}
