package project.inventorymanager.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import project.inventorymanager.model.file.StatisticFile;
import project.inventorymanager.testutil.SqlScriptPath;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StatisticFileRepositoryTest {
    @Autowired
    private StatisticFileRepository statisticFileRepository;

    @BeforeEach
    void setUp(@Autowired DataSource dataSource) throws SQLException {
        tearDown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(SqlScriptPath.INSERT_USER.getPath()));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(SqlScriptPath.INSERT_STATISTIC_FILE.getPath()));
        }
    }

    @Test
    @DisplayName("Find all statistic files with empty data")
    void findAll_findAllWithEmptyData_emptyPage(@Autowired DataSource dataSource) {
        tearDown(dataSource);
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        Page<StatisticFile> statisticFiles = statisticFileRepository.findAll(pageable);
        assertTrue(statisticFiles.isEmpty());
    }

    @Test
    @DisplayName("Find all statistic files with exist data")
    void findAll_findAllWithExistData_statisticFilesPage() {
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        Page<StatisticFile> statisticFiles = statisticFileRepository.findAll(pageable);
        assertNotNull(statisticFiles);
        assertEquals(3L, statisticFiles.getTotalElements());
    }

    @Test
    @DisplayName("Find statistic files by non exist id")
    void findById_findByIdWithNotExistData_emptyOptional() {
        Long id = -1L;
        Optional<StatisticFile> statisticFileOptional = statisticFileRepository.findById(id);
        assertTrue(statisticFileOptional.isEmpty());
    }

    @Test
    @DisplayName("Find statistic files by exist id")
    void findById_findByIdWithExistData_optionalOfStatisticFiles() {
        Long id = 1L;
        Optional<StatisticFile> statisticFileOptional = statisticFileRepository.findById(id);
        assertTrue(statisticFileOptional.isPresent());
        assertEquals(id, statisticFileOptional.get().getId());
    }

    @SneakyThrows
    @AfterEach
    void tearDown(@Autowired DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(SqlScriptPath.DELETE_DATA.getPath()));
        }
    }
}
