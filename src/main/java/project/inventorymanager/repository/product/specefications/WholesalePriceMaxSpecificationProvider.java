package project.inventorymanager.repository.product.specefications;

import java.math.BigDecimal;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import project.inventorymanager.model.product.Product;
import project.inventorymanager.repository.SpecificationProvider;

@Component
public class WholesalePriceMaxSpecificationProvider
        implements SpecificationProvider<Product, BigDecimal> {
    private static final String SPEC_KEY = "wholesalePriceMax";
    private static final String FIELD_NAME = "wholesalePrice";
    private static final BigDecimal MIN_VALUE = BigDecimal.valueOf(0);

    @Override
    public String getKey() {
        return SPEC_KEY;
    }

    @Override
    public Specification<Product> getSpecification(BigDecimal params) {
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .between(root.get(FIELD_NAME), MIN_VALUE, params);
    }
}
