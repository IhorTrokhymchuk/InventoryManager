package project.inventorymanager.dto.statisticfile.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;
import project.inventorymanager.dto.user.response.UserResponseDto;

@Data
public class StatisticFileResponseDto {
    private Long id;
    private UserResponseDto user;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private LocalDateTime createdAt;
    private String dropboxId;
}
