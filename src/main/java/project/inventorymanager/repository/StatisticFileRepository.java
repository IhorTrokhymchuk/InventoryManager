package project.inventorymanager.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import project.inventorymanager.model.file.StatisticFile;

public interface StatisticFileRepository extends JpaRepository<StatisticFile, Long> {
    @EntityGraph(attributePaths = {"user", "user.roles"})
    Optional<StatisticFile> findById(Long id);

    @EntityGraph(attributePaths = {"user", "user.roles"})
    Page<StatisticFile> findAll(Pageable pageable);
}
