package project.inventorymanager.repositoryservice.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import project.inventorymanager.exception.repository.EntityNotFoundException;
import project.inventorymanager.model.file.StatisticFile;
import project.inventorymanager.repository.StatisticFileRepository;
import project.inventorymanager.repositoryservice.StatisticFileRepoService;

@Repository
@RequiredArgsConstructor
public class StatisticFileRepoServiceImpl implements StatisticFileRepoService {
    private final StatisticFileRepository statisticFileRepository;

    @Override
    public Page<StatisticFile> findAll(Pageable pageable) {
        return statisticFileRepository.findAll(pageable);
    }

    @Override
    public StatisticFile getById(Long id) {
        return statisticFileRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Cant find statistic file with id: " + id));
    }

    @Override
    public StatisticFile save(StatisticFile statisticFile) {
        return statisticFileRepository.save(statisticFile);
    }
}
