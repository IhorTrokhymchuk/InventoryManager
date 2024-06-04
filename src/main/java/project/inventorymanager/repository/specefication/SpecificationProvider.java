package project.inventorymanager.repository.specefication;

import org.springframework.data.jpa.domain.Specification;

public interface SpecificationProvider<T, D> {
    String getKey();

    Specification<T> getSpecification(D params);
}
