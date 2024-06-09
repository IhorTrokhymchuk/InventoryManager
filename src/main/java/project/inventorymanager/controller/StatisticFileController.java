package project.inventorymanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import project.inventorymanager.dto.statisticfile.response.StatisticFileResponseDto;
import project.inventorymanager.service.StatisticFileService;

@Tag(name = "Inventories management", description = "Endpoints to managing inventories")
@RestController
@RequestMapping("/statistics-file")
@RequiredArgsConstructor
public class StatisticFileController {
    private final StatisticFileService statisticFileService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Get file by id", description = "Get available file by id")
    public StatisticFileResponseDto getById(@PathVariable Long id) {
        return statisticFileService.getById(id);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Get all files", description = "Get all available files")
    public List<StatisticFileResponseDto> findAll(Pageable pageable) {
        return statisticFileService.findAll(pageable);
    }

    @GetMapping("/download/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Get file by id", description = "Get available file by id")
    public String getDownloadUrl(@PathVariable Long id) {
        return statisticFileService.getDownloadUrl(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete file by id",
            description = "Delete available file by id from db and dropbox")
    public void deleteById(@PathVariable Long id) {
        statisticFileService.deleteById(id);
    }
}
