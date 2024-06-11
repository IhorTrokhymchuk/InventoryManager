package project.inventorymanager.repository.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import project.inventorymanager.model.product.Product;
import project.inventorymanager.testutil.SqlScriptPath;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp(@Autowired DataSource dataSource) throws SQLException {
        tearDown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(SqlScriptPath.INSERT_CATEGORIES.getPath()));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(SqlScriptPath.INSERT_PRODUCTS.getPath()));
        }
    }

    @Test
    @DisplayName("Find all products with empty data")
    void findAll_findAllProductsWithEmptyData_emptyPage(@Autowired DataSource dataSource) {
        tearDown(dataSource);
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        Page<Product> products = productRepository.findAll(pageable);
        assertTrue(products.isEmpty());
    }

    @Test
    @DisplayName("Find all products with exist data")
    void findAll_findAllProductsWithExistData_productsPage() {
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        Page<Product> products = productRepository.findAll(pageable);
        assertNotNull(products);
        assertEquals(2L, products.getTotalElements());
    }

    @Test
    @DisplayName("Find all products by exist id specification")
    void findAll_findAllProductsBySpecificationWithExistData_productsPage() {
        Specification<Product> specification = getProductIdSpecification(1L);

        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        Page<Product> products = productRepository.findAll(specification, pageable);
        assertNotNull(products);
        assertEquals(1L, products.getTotalElements());
        assertEquals(1L, products.stream().toList().get(0).getId());
    }

    @Test
    @DisplayName("Find all products by non exist id specification")
    void findAll_findAllProductsBySpecificationWithNotExistData_emptyProduct() {
        Specification<Product> specification = getProductIdSpecification(-1L);

        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        Page<Product> products = productRepository.findAll(specification, pageable);
        assertTrue(products.isEmpty());
    }

    @Test
    @DisplayName("Find products by non exist id")
    void findById_findByIdProductsWithNotExistData_emptyOptional() {
        Long id = -1L;
        Optional<Product> productOptional = productRepository.findById(id);
        assertTrue(productOptional.isEmpty());
    }

    @Test
    @DisplayName("Find products by exist id")
    void findById_findByIdProductsWithExistData_optionalOfProduct() {
        Long id = 1L;
        Optional<Product> productOptional = productRepository.findById(id);
        assertTrue(productOptional.isPresent());
        assertEquals(id, productOptional.get().getId());
    }

    @Test
    @DisplayName("Check exist by unicode with deleted data")
    void existsByUniqCode_existsByUniqCodeWithDeletedData_false() {
        String uniqCode = "PRODUCT3";
        boolean exists = productRepository.existsByUniqCode(uniqCode);
        assertFalse(exists);
    }

    @Test
    @DisplayName("Check exist by unicode with non exist data")
    void existsByUniqCode_existsByUniqCodeWithNonExistData_false() {
        String uniqCode = "PRODUCT3qweqwe";
        boolean exists = productRepository.existsByUniqCode(uniqCode);
        assertFalse(exists);
    }

    @Test
    @DisplayName("Check exist by unicode with exist data")
    void existsByUniqCode_existsByUniqCodeWithExistData_false() {
        String uniqCode = "PRODUCT2";
        boolean exists = productRepository.existsByUniqCode(uniqCode);
        assertTrue(exists);
    }

    @Test
    @DisplayName("Count exist by unicode and is deleted false")
    void existsByUniqCodeIncludingDeleted_countProductsByUniqCodeAndIsDeletedIsFalse_one() {
        String uniqCode = "PRODUCT2";
        Long count = productRepository.existsByUniqCodeIncludingDeleted(uniqCode);
        assertEquals(1, count);
    }

    @Test
    @DisplayName("Count exist by unicode and is deleted true")
    void existsByUniqCodeIncludingDeleted_countProductsByUniqCodeAndIsDeletedIsTrue_one() {
        String uniqCode = "PRODUCT3";
        Long count = productRepository.existsByUniqCodeIncludingDeleted(uniqCode);
        assertEquals(1, count);
    }

    @Test
    @DisplayName("Find product where is deleted is true and exist unicode")
    void findDeletedByUniqCode_findByUnicodeIsExistAndIsDeletedIsTrue_product() {
        String uniqCode = "PRODUCT3";
        Optional<Product> deletedByUniqCode = productRepository.findDeletedByUniqCode(uniqCode);
        assertTrue(deletedByUniqCode.isPresent());
        Product product = deletedByUniqCode.get();
        assertTrue(product.isDeleted());
        assertEquals(uniqCode, product.getUniqCode());
    }

    @Test
    @DisplayName("Find product where is deleted is false and exist unicode")
    void findDeletedByUniqCode_findByUnicodeIsExistAndIsDeletedIsFalse_emptyOptional() {
        String uniqCode = "PRODUCT2";
        Optional<Product> deletedByUniqCode = productRepository.findDeletedByUniqCode(uniqCode);
        assertTrue(deletedByUniqCode.isEmpty());
    }

    @Test
    @DisplayName("Find product where non exist data")
    void findDeletedByUniqCode_findByUnicodeIsNotExist_emptyOptional() {
        String uniqCode = "PRODUCT2eqweqwweqwe";
        Optional<Product> deletedByUniqCode = productRepository.findDeletedByUniqCode(uniqCode);
        assertTrue(deletedByUniqCode.isEmpty());
    }

    @NotNull
    private Specification<Product> getProductIdSpecification(Long id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id);
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
