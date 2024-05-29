package project.inventorymanager.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.inventorymanager.dto.statisticfile.response.StatisticFileResponseDto;
import project.inventorymanager.mapper.StatisticFileMapper;
import project.inventorymanager.repositoryservice.StatisticFileRepoService;
import project.inventorymanager.service.StatisticFileService;
import project.inventorymanager.util.DropboxUtil;

@Service
@RequiredArgsConstructor
public class StatisticFileServiceImpl implements StatisticFileService {
    private final StatisticFileRepoService statisticFileRepoService;
    private final StatisticFileMapper statisticFileMapper;
    private final DropboxUtil dropboxUtil;

    @Override
    public List<StatisticFileResponseDto> findAll(Pageable pageable) {
        return statisticFileRepoService.findAll(pageable).stream()
                .map(statisticFileMapper::toResponseDto)
                .toList();
    }

    @Override
    public String getDownloadUrl(Long id) {
        String dropboxId = statisticFileRepoService.getById(id).getDropboxId();
        return dropboxUtil.getDownloadUrl(dropboxId);
    }

    @Override
    public StatisticFileResponseDto getById(Long id) {
        return statisticFileMapper.toResponseDto(statisticFileRepoService.getById(id));
    }
}
