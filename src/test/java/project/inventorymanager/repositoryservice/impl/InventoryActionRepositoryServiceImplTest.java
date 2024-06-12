package project.inventorymanager.repositoryservice.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
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
import project.inventorymanager.model.inventoryaction.InventoryAction;
import project.inventorymanager.repository.InventoryActionRepository;

@ExtendWith(SpringExtension.class)
public class InventoryActionRepositoryServiceImplTest {
    @Mock
    private InventoryActionRepository inventoryActionRepository;
    @InjectMocks
    private InventoryActionRepositoryServiceImpl inventoryActionRepositoryService;

    @Test
    @DisplayName("Save inventory action successfully")
    public void save_saveInventoryAction_savedInventoryAction() {
        InventoryAction inventoryAction = new InventoryAction();
        when(inventoryActionRepository.save(inventoryAction)).thenReturn(inventoryAction);

        InventoryAction savedAction = inventoryActionRepositoryService.save(inventoryAction);

        assertNotNull(savedAction);
        verify(inventoryActionRepository, times(1)).save(inventoryAction);
    }

    @Test
    @DisplayName("Get inventory action by id successfully")
    public void getById_getByIdWithExistData_inventoryAction() {
        Long id = 1L;
        InventoryAction inventoryAction = new InventoryAction();
        when(inventoryActionRepository.findById(id)).thenReturn(Optional.of(inventoryAction));

        InventoryAction foundAction = inventoryActionRepositoryService.getById(id);

        assertNotNull(foundAction);
        verify(inventoryActionRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Get inventory action by id throws EntityNotFoundException")
    public void getById_getByIdWithNonExistData_exception() {
        Long id = 1L;
        when(inventoryActionRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class, () -> inventoryActionRepositoryService.getById(id));

        assertEquals("Cant find inventory action with id: " + id, exception.getMessage());
        verify(inventoryActionRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("FindAll inventory actions")
    public void findAll_findAllInventoryActionWithExistData_inventoryActionList() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<InventoryAction> page = new PageImpl<>(List.of(new InventoryAction()));
        when(inventoryActionRepository.findAll(pageable)).thenReturn(page);

        Page<InventoryAction> result = inventoryActionRepositoryService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(inventoryActionRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("GetAllByDates returns inventory actions successfully")
    public void getAllByDates_getAllByDatesWithExistData_inventoryActionList() {
        LocalDate fromDate = LocalDate.now().minusDays(10);
        LocalDate toDate = LocalDate.now();
        List<InventoryAction> actions = List.of(new InventoryAction());
        when(inventoryActionRepository.findAllByDates(fromDate, toDate)).thenReturn(actions);

        List<InventoryAction> result
                = inventoryActionRepositoryService.getAllByDates(fromDate, toDate);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(inventoryActionRepository, times(1)).findAllByDates(fromDate, toDate);
    }

    @Test
    @DisplayName("GetAllByDates throws EntityNotFoundException")
    public void getAllByDates_getAllByDatesWithNonExistData_exception() {
        LocalDate fromDate = LocalDate.now().minusDays(10);
        LocalDate toDate = LocalDate.now();
        when(inventoryActionRepository.findAllByDates(fromDate, toDate)).thenReturn(List.of());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class, () ->
                        inventoryActionRepositoryService.getAllByDates(fromDate, toDate));

        assertEquals("Cant find inventory actions by from date: "
                + fromDate + " to date: " + toDate, exception.getMessage());
        verify(inventoryActionRepository, times(1)).findAllByDates(fromDate, toDate);
    }
}
