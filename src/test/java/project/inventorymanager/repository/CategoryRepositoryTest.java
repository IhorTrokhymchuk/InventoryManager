package project.inventorymanager.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;
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
import org.springframework.jdbc.datasource.init.ScriptUtils;
import project.inventorymanager.model.product.Category;
import project.inventorymanager.testutil.SqlScriptPath;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp(@Autowired DataSource dataSource) throws SQLException {
        tearDown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(SqlScriptPath.INSERT_CATEGORIES.getPath()));
        }
    }

    @Test
    @DisplayName("Find all categories by IDs")
    void findAllByIdIn_findAllCategoriesByIds_productSet() {
        Set<Long> categoryIds = Set.of(1L, 2L);
        Set<Category> categories = categoryRepository.findAllByIdIn(categoryIds);
        assertNotNull(categories);
        assertEquals(2, categories.size());
    }

    @Test
    @DisplayName("Check if category exists by name")
    void existsByName_checkIfCategoryExistsByName_true() {
        String name = "Business";
        boolean exists = categoryRepository.existsByName(name);
        assertTrue(exists);
    }

    @Test
    @DisplayName("Check if category exists by name including deleted")
    void existsByNameIncludingDeleted_checkIfCategoryExistsByNameIncludingDeleted_one() {
        String name = "Business";
        Long count = categoryRepository.existsByNameIncludingDeleted(name);
        assertEquals(1, count);
    }

    @Test
    @DisplayName("Find deleted category by name")
    void findDeletedByName_findDeletedCategoryByName_categoryOptional() {
        String name = "Building";
        Optional<Category> categoryOptional = categoryRepository.findDeletedByName(name);
        assertTrue(categoryOptional.isPresent());
        Category category = categoryOptional.get();
        assertTrue(category.isDeleted());
        assertEquals(name, category.getName());
    }

    @Test
    @DisplayName("Find non-existing category by name")
    void findDeletedByName_findNonExistingCategoryByName_emptyOptional() {
        String name = "NonExistentCategory";
        Optional<Category> optionalCategory = categoryRepository.findDeletedByName(name);
        assertTrue(optionalCategory.isEmpty());
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
