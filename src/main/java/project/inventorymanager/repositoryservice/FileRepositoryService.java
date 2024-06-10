package project.inventorymanager.repositoryservice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.inventorymanager.model.file.StatisticFile;

public interface FileRepositoryService {
    Page<StatisticFile> findAll(Pageable pageable);

    StatisticFile getById(Long id);

    StatisticFile save(StatisticFile statisticFile);

    void deleteById(Long id);
}
