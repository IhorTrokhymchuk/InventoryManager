package project.inventorymanager.repository;

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
import project.inventorymanager.model.inventoryaction.InventoryAction;
import project.inventorymanager.testutil.SqlScriptPath;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class InventoryActionRepositoryTest {
    @Autowired
    private InventoryActionRepository inventoryActionRepository;

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
                    new ClassPathResource(SqlScriptPath.INSERT_INVENTORY_ACTIONS.getPath()));
        }
    }

    @Test
    @DisplayName("Find all inventory actions with empty data")
    void findAll_findAllProductsWithEmptyData_emptyPage(@Autowired DataSource dataSource) {
        tearDown(dataSource);
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        Page<InventoryAction> products = inventoryActionRepository.findAll(pageable);
        assertTrue(products.isEmpty());
    }

    @Test
    @DisplayName("Find all inventory actions with exist data")
    void findAll_findAllWithExistData_inventoryActionsPage() {
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        Page<InventoryAction> products = inventoryActionRepository.findAll(pageable);
        assertNotNull(products);
        assertEquals(5L, products.getTotalElements());
    }

    @Test
    @DisplayName("Find inventory actions by non exist id")
    void findById_findByIdWithNotExistData_emptyOptional() {
        Long id = -1L;
        Optional<InventoryAction> productOptional = inventoryActionRepository.findById(id);
        assertTrue(productOptional.isEmpty());
    }

    @Test
    @DisplayName("Find inventory actions by exist id")
    void findById_findByIdWithExistData_optionalOfInventoryActions() {
        Long id = 1L;
        Optional<InventoryAction> productOptional = inventoryActionRepository.findById(id);
        assertTrue(productOptional.isPresent());
        assertEquals(id, productOptional.get().getId());
    }

    @Test
    @DisplayName("Find inventory actions by date with non exist data")
    void findAllByDates_findAllByDatesWithNonExistData_emptyList() {
        LocalDate dateFrom = LocalDate.of(2024,1,1);
        LocalDate dateTo = LocalDate.of(2024,1,3);
        List<InventoryAction> inventoryActionList
                = inventoryActionRepository.findAllByDates(dateFrom, dateTo);
        assertTrue(inventoryActionList.isEmpty());
    }

    @Test
    @DisplayName("Find inventory actions by date with exist data")
    void findAllByDates_findAllByDatesWithExistData_inventoryActionList() {
        LocalDate dateFrom = LocalDate.of(2024,6,10);
        LocalDate dateTo = LocalDate.of(2024,6,14);
        List<InventoryAction> inventoryActionList
                = inventoryActionRepository.findAllByDates(dateFrom, dateTo);
        assertEquals(5, inventoryActionList.size());
    }

    @Test
    @DisplayName("Find inventory actions by date with exist data and date to")
    void findAllByDates_findAllByDatesWithExistDataAndDateTo_inventoryActionList() {
        LocalDate dateFrom = LocalDate.of(2024,6,10);
        LocalDate dateTo = LocalDate.of(2024,6,12);
        List<InventoryAction> inventoryActionList
                = inventoryActionRepository.findAllByDates(dateFrom, dateTo);
        assertEquals(4, inventoryActionList.size());
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