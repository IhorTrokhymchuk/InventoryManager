package project.inventorymanager.repository.specefication;

import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T, D> {
    Specification<T> build(D requestDto);
}
