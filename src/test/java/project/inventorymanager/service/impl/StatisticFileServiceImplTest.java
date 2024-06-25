package project.inventorymanager.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static project.inventorymanager.testutil.objects.StatisticFileProvider.getStatisticFile;
import static project.inventorymanager.testutil.objects.StatisticFileProvider.getStatisticFileResponseDto;

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
import project.inventorymanager.dropbox.DropboxUtil;
import project.inventorymanager.dto.statisticfile.response.StatisticFileResponseDto;
import project.inventorymanager.mapper.StatisticFileMapper;
import project.inventorymanager.model.file.StatisticFile;
import project.inventorymanager.repositoryservice.FileRepositoryService;

@ExtendWith(MockitoExtension.class)
public class StatisticFileServiceImplTest {
    @Mock
    private FileRepositoryService fileRepositoryService;
    @Mock
    private StatisticFileMapper statisticFileMapper;
    @Mock
    private DropboxUtil dropboxUtil;
    @InjectMocks
    private StatisticFileServiceImpl statisticFileService;
    private StatisticFile statisticFile;
    private StatisticFileResponseDto responseDto;

    @BeforeEach
    void setup() {
        Long id = 1L;
        statisticFile = getStatisticFile(id);
        responseDto = getStatisticFileResponseDto(id);
    }

    @Test
    @DisplayName("Find all statistic files with valid pageable")
    void findAll_withValidPageable_statisticFileList() {
        Pageable pageable = mock(Pageable.class);
        List<StatisticFile> statisticFiles = List.of(statisticFile);
        Page<StatisticFile> page = new PageImpl<>(statisticFiles);
        when(fileRepositoryService.findAll(pageable)).thenReturn(page);
        when(statisticFileMapper.toResponseDto(statisticFile)).thenReturn(responseDto);

        List<StatisticFileResponseDto> result = statisticFileService.findAll(pageable);

        assertEquals(List.of(responseDto), result);
        verify(fileRepositoryService, times(1)).findAll(pageable);
        verify(statisticFileMapper, times(statisticFiles.size())).toResponseDto(statisticFile);
        verifyNoMoreInteractions(fileRepositoryService, statisticFileMapper, dropboxUtil);
    }

    @Test
    @DisplayName("Get download URL for valid file id")
    void getDownloadUrl_withValidId_downloadUrl() {
        Long id = 1L;
        when(fileRepositoryService.getById(id)).thenReturn(statisticFile);
        when(dropboxUtil.getDownloadUrl(statisticFile.getDropboxId())).thenReturn("download-url");

        String result = statisticFileService.getDownloadUrl(id);

        assertEquals("download-url", result);
        verify(fileRepositoryService, times(1)).getById(id);
        verify(dropboxUtil, times(1)).getDownloadUrl(statisticFile.getDropboxId());
        verifyNoMoreInteractions(fileRepositoryService, statisticFileMapper, dropboxUtil);
    }

    @Test
    @DisplayName("Delete statistic file by valid id")
    void deleteById_withValidId_delete() {
        Long id = 1L;
        when(fileRepositoryService.getById(id)).thenReturn(statisticFile);

        statisticFileService.deleteById(id);

        verify(fileRepositoryService, times(1)).getById(id);
        verify(dropboxUtil, times(1)).deleteFile(statisticFile.getDropboxId());
        verify(fileRepositoryService, times(1)).deleteById(id);
        verifyNoMoreInteractions(fileRepositoryService, statisticFileMapper, dropboxUtil);
    }

    @Test
    @DisplayName("Get Statistic File by valid id")
    void getById_withValidId_statisticFile() {
        Long id = 1L;

        when(fileRepositoryService.getById(id)).thenReturn(statisticFile);
        when(statisticFileMapper.toResponseDto(statisticFile)).thenReturn(responseDto);

        StatisticFileResponseDto result = statisticFileService.getById(id);

        assertEquals(responseDto, result);
        verify(fileRepositoryService, times(1)).getById(id);
        verify(statisticFileMapper, times(1)).toResponseDto(statisticFile);
        verifyNoMoreInteractions(fileRepositoryService, statisticFileMapper, dropboxUtil);
    }
}
