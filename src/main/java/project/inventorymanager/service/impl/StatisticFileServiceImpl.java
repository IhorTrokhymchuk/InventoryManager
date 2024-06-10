package project.inventorymanager.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.inventorymanager.dropbox.DropboxUtil;
import project.inventorymanager.dto.statisticfile.response.StatisticFileResponseDto;
import project.inventorymanager.mapper.StatisticFileMapper;
import project.inventorymanager.model.file.StatisticFile;
import project.inventorymanager.repositoryservice.FileRepositoryService;
import project.inventorymanager.service.StatisticFileService;

@Service
@RequiredArgsConstructor
public class StatisticFileServiceImpl implements StatisticFileService {
    private final FileRepositoryService fileRepositoryService;
    private final StatisticFileMapper statisticFileMapper;
    private final DropboxUtil dropboxUtil;

    @Override
    public List<StatisticFileResponseDto> findAll(Pageable pageable) {
        return fileRepositoryService.findAll(pageable).stream()
                .map(statisticFileMapper::toResponseDto)
                .toList();
    }

    @Override
    public String getDownloadUrl(Long id) {
        String dropboxId = fileRepositoryService.getById(id).getDropboxId();
        return dropboxUtil.getDownloadUrl(dropboxId);
    }

    @Override
    public void deleteById(Long id) {
        StatisticFile file = fileRepositoryService.getById(id);
        String dropboxId = file.getDropboxId();
        dropboxUtil.deleteFile(dropboxId);
        fileRepositoryService.deleteById(id);
    }

    @Override
    public StatisticFileResponseDto getById(Long id) {
        return statisticFileMapper.toResponseDto(fileRepositoryService.getById(id));
    }

}
