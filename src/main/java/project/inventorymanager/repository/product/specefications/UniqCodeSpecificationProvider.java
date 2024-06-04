package project.inventorymanager.repository.product.specefications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import project.inventorymanager.model.product.Product;
import project.inventorymanager.repository.specefication.SpecificationProvider;

@Component
public class UniqCodeSpecificationProvider
        implements SpecificationProvider<Product, String> {
    private static final String SPEC_KEY = "uniqCode";
    private static final String FIELD_NAME = "uniqCode";

    @Override
    public String getKey() {
        return SPEC_KEY;
    }

    @Override
    public Specification<Product> getSpecification(String param) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(FIELD_NAME),
                "%" + param + "%");
    }
}
