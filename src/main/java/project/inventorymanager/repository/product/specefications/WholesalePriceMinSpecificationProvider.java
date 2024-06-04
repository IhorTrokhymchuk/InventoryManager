package project.inventorymanager.repository.product.specefications;

import java.math.BigDecimal;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import project.inventorymanager.model.product.Product;
import project.inventorymanager.repository.specefication.SpecificationProvider;

@Component
public class WholesalePriceMinSpecificationProvider
        implements SpecificationProvider<Product, BigDecimal> {
    private static final String SPEC_KEY = "wholesalePriceMin";
    private static final String FIELD_NAME = "wholesalePrice";
    private static final BigDecimal MAX_VALUE = BigDecimal.valueOf(Long.MAX_VALUE);

    @Override
    public String getKey() {
        return SPEC_KEY;
    }

    @Override
    public Specification<Product> getSpecification(BigDecimal params) {
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .between(root.get(FIELD_NAME), params, MAX_VALUE);
    }
}
