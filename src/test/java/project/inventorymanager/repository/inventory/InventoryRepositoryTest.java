package project.inventorymanager.repository.inventory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.transaction.Transactional;
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
        Page<Inventory> inventories = inventoryRepository.findAll(pageable);
        assertTrue(inventories.isEmpty());
    }

    @Test
    @DisplayName("Find all inventories with exist data")
    void findAll_findAllInventoriesWithExistData_inventoriesPage() {
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        Page<Inventory> inventories = inventoryRepository.findAll(pageable);
        assertNotNull(inventories);
        assertEquals(3L, inventories.stream().toList().size());
    }

    @Test
    @DisplayName("Find inventory by id with empty data")
    void findById_findByIdInventoryWithEmptyData_emptyOptional() {
        Long id = -1L;
        Optional<Inventory> inventoryOptional = inventoryRepository.findById(id);
        assertTrue(inventoryOptional.isEmpty());
    }

    @Test
    @DisplayName("Find inventory by id with exist data")
    void findById_findByIdInventoriesWithExistData_optionalOfInventory() {
        Long id = 1L;
        Optional<Inventory> inventoryOptional = inventoryRepository.findById(id);
        assertTrue(inventoryOptional.isPresent());
        assertEquals(id, inventoryOptional.get().getId());
    }

    @Test
    @DisplayName("Find inventory by product id and warehouse id with empty data")
    void findByProductIdAndWarehouseId_WithEmptyData_emptyOptional() {
        Long productId = -1L;
        Long warehouseId = -1L;
        Optional<Inventory> inventoryOptional
                = inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId);
        assertTrue(inventoryOptional.isEmpty());
    }

    @Test
    @DisplayName("Find inventory by product id and warehouse id with exist data")
    @Transactional
    void findByProductIdAndWarehouseId_WithExistData_optionalOfInventory() {
        Long productId = 2L;
        Long warehouseId = 2L;
        Optional<Inventory> inventoryOptional
                = inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId);
        assertTrue(inventoryOptional.isPresent());
        assertEquals(productId, inventoryOptional.get().getProduct().getId());
        assertEquals(warehouseId, inventoryOptional.get().getWarehouse().getId());
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
