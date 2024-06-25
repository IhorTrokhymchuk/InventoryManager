package project.inventorymanager.testutil.objects;

import static project.inventorymanager.testutil.objects.UserProvider.getUser;
import static project.inventorymanager.testutil.objects.UserProvider.getUserResponseDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import project.inventorymanager.dto.statisticfile.response.StatisticFileResponseDto;
import project.inventorymanager.model.file.StatisticFile;

public class StatisticFileProvider {
    private static final String DROPBOX_ID = "example";

    public static StatisticFile getStatisticFile(Long id) {
        StatisticFile statisticFile = new StatisticFile();
        statisticFile.setId(id);
        statisticFile.setUser(getUser(id));
        statisticFile.setDateFrom(LocalDate.now().minusDays(3L));
        statisticFile.setDateTo(LocalDate.now());
        statisticFile.setCreatedAt(LocalDateTime.now());
        statisticFile.setDropboxId(DROPBOX_ID + id);
        return statisticFile;
    }

    public static StatisticFileResponseDto getStatisticFileResponseDto(Long id) {
        StatisticFileResponseDto statisticFileResponseDto = new StatisticFileResponseDto();
        statisticFileResponseDto.setId(id);
        statisticFileResponseDto.setUser(getUserResponseDto(id));
        statisticFileResponseDto.setDateFrom(LocalDate.now().minusDays(3L));
        statisticFileResponseDto.setDateTo(LocalDate.now());
        statisticFileResponseDto.setCreatedAt(LocalDateTime.now());
        statisticFileResponseDto.setDropboxId(DROPBOX_ID + id);
        return statisticFileResponseDto;
    }
}
