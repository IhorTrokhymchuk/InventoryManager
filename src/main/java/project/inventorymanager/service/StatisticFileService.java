package project.inventorymanager.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import project.inventorymanager.dto.statisticfile.response.StatisticFileResponseDto;

public interface StatisticFileService {
    List<StatisticFileResponseDto> findAll(Pageable pageable);

    StatisticFileResponseDto getById(Long id);

    String getDownloadUrl(Long id);

    void deleteById(Long id);
}
