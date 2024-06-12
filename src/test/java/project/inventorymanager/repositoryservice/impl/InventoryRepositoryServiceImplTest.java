package project.inventorymanager.repositoryservice.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import project.inventorymanager.exception.repository.EntityNotFoundException;
import project.inventorymanager.model.inventory.Inventory;
import project.inventorymanager.repository.inventory.InventoryRepository;

@ExtendWith(SpringExtension.class)
public class InventoryRepositoryServiceImplTest {
    @Mock
    private InventoryRepository inventoryRepository;
    @InjectMocks
    private InventoryRepositoryServiceImpl inventoryRepositoryService;

    @Test
    @DisplayName("Test save inventory successfully")
    public void testSaveInventory() {
        Inventory inventory = new Inventory();
        when(inventoryRepository.save(inventory)).thenReturn(inventory);

        Inventory savedInventory = inventoryRepositoryService.save(inventory);

        assertNotNull(savedInventory);
        verify(inventoryRepository, times(1)).save(inventory);
    }

    @Test
    @DisplayName("Test get inventory by productId and warehouseId successfully")
    public void testGetByProductIdAndWarehouseId() {
        Long productId = 1L;
        Long warehouseId = 1L;
        Inventory inventory = new Inventory();
        when(inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId))
                .thenReturn(Optional.of(inventory));

        Inventory foundInventory = inventoryRepositoryService
                .getByProductIdAndWarehouseId(productId, warehouseId);

        assertNotNull(foundInventory);
        verify(inventoryRepository, times(1)).findByProductIdAndWarehouseId(productId, warehouseId);
    }

    @Test
    @DisplayName("Test get inventory by productId and warehouseId throws EntityNotFoundException")
    public void testGetByProductIdAndWarehouseIdNotFound() {
        Long productId = 1L;
        Long warehouseId = 1L;
        when(inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            inventoryRepositoryService.getByProductIdAndWarehouseId(productId, warehouseId);
        });

        assertEquals("Cant find product with id: " + productId + " on warehouse with id: "
                + warehouseId, exception.getMessage());
        verify(inventoryRepository, times(1)).findByProductIdAndWarehouseId(productId, warehouseId);
    }

    @Test
    @DisplayName("Test findAll inventories")
    public void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Inventory> page = new PageImpl<>(List.of(new Inventory()));
        when(inventoryRepository.findAll(pageable)).thenReturn(page);

        Page<Inventory> result = inventoryRepositoryService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(inventoryRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Test get inventory by id successfully")
    public void testGetById() {
        Long id = 1L;
        Inventory inventory = new Inventory();
        when(inventoryRepository.findById(id)).thenReturn(Optional.of(inventory));

        Inventory foundInventory = inventoryRepositoryService.getById(id);

        assertNotNull(foundInventory);
        verify(inventoryRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Test get inventory by id throws EntityNotFoundException")
    public void testGetByIdNotFound() {
        Long id = 1L;
        when(inventoryRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            inventoryRepositoryService.getById(id);
        });

        assertEquals("Cant find inventory with id: " + id, exception.getMessage());
        verify(inventoryRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Test find inventory by productId and warehouseId successfully")
    public void testFindByProductIdAndWarehouseId() {
        Long productId = 1L;
        Long warehouseId = 1L;
        Inventory inventory = new Inventory();
        when(inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId))
                .thenReturn(Optional.of(inventory));

        Optional<Inventory> foundInventory = inventoryRepositoryService
                .findByProductIdAndWarehouseId(productId, warehouseId);

        assertTrue(foundInventory.isPresent());
        verify(inventoryRepository, times(1)).findByProductIdAndWarehouseId(productId, warehouseId);
    }
}
