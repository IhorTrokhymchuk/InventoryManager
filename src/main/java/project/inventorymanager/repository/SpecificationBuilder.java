package project.inventorymanager.repository;

import org.springframework.data.jpa.domain.Specification;
import project.inventorymanager.dto.product.request.ProductSearchDto;

public interface SpecificationBuilder<T> {
    Specification<T> build(ProductSearchDto requestDto);
}
