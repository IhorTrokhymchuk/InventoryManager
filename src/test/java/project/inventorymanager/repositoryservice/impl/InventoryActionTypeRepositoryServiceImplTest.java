package project.inventorymanager.repositoryservice.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import project.inventorymanager.exception.repository.EntityNotFoundException;
import project.inventorymanager.model.inventoryaction.InventoryActionType;
import project.inventorymanager.repository.InventoryActionTypeRepository;

@ExtendWith(SpringExtension.class)
public class InventoryActionTypeRepositoryServiceImplTest {
    @Mock
    private InventoryActionTypeRepository inventoryActionTypeRepository;
    @InjectMocks
    private InventoryActionTypeRepositoryServiceImpl inventoryActionTypeRepositoryService;

    @Test
    @DisplayName("Get inventory action type by id successfully")
    public void getById_getByIdWithExistData_inventoryActionType() {
        Long id = 1L;
        InventoryActionType inventoryActionType = new InventoryActionType();
        when(inventoryActionTypeRepository.findById(id))
                .thenReturn(Optional.of(inventoryActionType));

        InventoryActionType foundType = inventoryActionTypeRepositoryService.getById(id);

        assertNotNull(foundType);
        verify(inventoryActionTypeRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Get inventory action type by id throws EntityNotFoundException")
    public void getById_getByIdWithNonExistData_exception() {
        Long id = 1L;
        when(inventoryActionTypeRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                inventoryActionTypeRepositoryService.getById(id));

        assertEquals("Cant find inventory action type with id: " + id, exception.getMessage());
        verify(inventoryActionTypeRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Get inventory action type by type name successfully")
    public void getByTypeName_getByTypeNameWithExistData_inventoryActionType() {
        InventoryActionType.InventoryActionTypeName name
                = InventoryActionType.InventoryActionTypeName.REPLENISHMENT;
        InventoryActionType inventoryActionType = new InventoryActionType();
        when(inventoryActionTypeRepository
                .findInventoryActionTypeByName(name)).thenReturn(inventoryActionType);

        InventoryActionType foundType = inventoryActionTypeRepositoryService.getByTypeName(name);

        assertNotNull(foundType);
        verify(inventoryActionTypeRepository, times(1)).findInventoryActionTypeByName(name);
    }
}
