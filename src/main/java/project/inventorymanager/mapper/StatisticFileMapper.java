package project.inventorymanager.mapper;

import org.mapstruct.Mapper;
import project.inventorymanager.config.MapperConfig;
import project.inventorymanager.dto.statisticfile.response.StatisticFileResponseDto;
import project.inventorymanager.model.file.StatisticFile;

@Mapper(config = MapperConfig.class, uses = {UserMapper.class})
public interface StatisticFileMapper {
    StatisticFileResponseDto toResponseDto(StatisticFile statisticFile);
}
