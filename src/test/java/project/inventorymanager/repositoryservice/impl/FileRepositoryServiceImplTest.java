package project.inventorymanager.repositoryservice.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
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
import project.inventorymanager.model.file.StatisticFile;
import project.inventorymanager.repository.StatisticFileRepository;

@ExtendWith(SpringExtension.class)
public class FileRepositoryServiceImplTest {
    @Mock
    private StatisticFileRepository statisticFileRepository;
    @InjectMocks
    private FileRepositoryServiceImpl fileRepositoryService;

    @Test
    @DisplayName("Test findAll files successfully")
    public void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<StatisticFile> page = new PageImpl<>(List.of(new StatisticFile()));
        when(statisticFileRepository.findAll(pageable)).thenReturn(page);

        Page<StatisticFile> result = fileRepositoryService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(statisticFileRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Test get file by id successfully")
    public void testGetById() {
        Long id = 1L;
        StatisticFile statisticFile = new StatisticFile();
        when(statisticFileRepository.findById(id)).thenReturn(Optional.of(statisticFile));

        StatisticFile foundFile = fileRepositoryService.getById(id);

        assertNotNull(foundFile);
        verify(statisticFileRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Test get file by id throws EntityNotFoundException")
    public void testGetByIdNotFound() {
        Long id = 1L;
        when(statisticFileRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class, () -> fileRepositoryService.getById(id));

        assertEquals("Cant find statistic file with id: " + id, exception.getMessage());
        verify(statisticFileRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Test delete file by id successfully")
    public void testDeleteById() {
        Long id = 1L;
        doNothing().when(statisticFileRepository).deleteById(id);

        fileRepositoryService.deleteById(id);

        verify(statisticFileRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Test save file successfully")
    public void testSaveFile() {
        StatisticFile statisticFile = new StatisticFile();
        when(statisticFileRepository.save(statisticFile)).thenReturn(statisticFile);

        StatisticFile savedFile = fileRepositoryService.save(statisticFile);

        assertNotNull(savedFile);
        verify(statisticFileRepository, times(1)).save(statisticFile);
    }
}
