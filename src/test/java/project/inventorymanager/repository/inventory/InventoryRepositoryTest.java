package project.inventorymanager.repository.inventory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
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
import project.inventorymanager.model.inventory.Inventory;
import project.inventorymanager.testutil.SqlScriptPath;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class InventoryRepositoryTest {

    @Autowired
    private InventoryRepository inventoryRepository;

    @BeforeEach
    void setUp(@Autowired DataSource dataSource) throws SQLException {
        tearDown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(SqlScriptPath.INSERT_CATEGORIES.getPath()));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(SqlScriptPath.INSERT_PRODUCTS.getPath()));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(SqlScriptPath.INSERT_WAREHOUSES.getPath()));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(SqlScriptPath.INSERT_INVENTORIES.getPath()));
        }
    }

    @Test
    @DisplayName("Find all inventories with empty data")
    void findAll_findAllInventoriesWithEmptyData_emptyPage(@Autowired DataSource dataSource) {
        tearDown(dataSource);
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        Page<Inventory> allByUserEmail = inventoryRepository.findAll(pageable);
        assertTrue(allByUserEmail.isEmpty());
    }

    @Test
    @DisplayName("Find all inventories with exist data")
    void findAll_findAllInventoriesWithExistData_inventoriesPage() {
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        Page<Inventory> allByUserEmail = inventoryRepository.findAll(pageable);
        assertNotNull(allByUserEmail);
        assertEquals(3L, allByUserEmail.stream().toList().size());
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
