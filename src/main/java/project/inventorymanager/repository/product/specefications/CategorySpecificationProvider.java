package project.inventorymanager.repository.product.specefications;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import project.inventorymanager.model.product.Category;
import project.inventorymanager.model.product.Product;
import project.inventorymanager.repository.SpecificationProvider;

@Component
public class CategorySpecificationProvider
        implements SpecificationProvider<Product, Long[]> {
    private static final String SPEC_KEY = "categoryIds";
    private static final String FIELD_NAME = "categories";

    @Override
    public String getKey() {
        return SPEC_KEY;
    }

    @Override
    public Specification<Product> getSpecification(Long[] params) {
        return (root, query, criteriaBuilder) -> {
            Join<Product, Category> amenityJoin = root.join(FIELD_NAME);
            List<Predicate> predicates = new ArrayList<>();
            for (Long categoryId : params) {
                predicates.add(criteriaBuilder.equal(amenityJoin.get("id"), categoryId));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
