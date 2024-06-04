package project.inventorymanager.repository.product;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import project.inventorymanager.dto.product.request.ProductSearchDto;
import project.inventorymanager.model.product.Product;
import project.inventorymanager.repository.SpecificationBuilder;
import project.inventorymanager.repository.SpecificationProviderManager;

@RequiredArgsConstructor
@Component
public class ProductSpecificationBuilder implements SpecificationBuilder<Product> {
    private final SpecificationProviderManager<Product, Long[]>
            specificationProviderManagerLongArr;
    private final SpecificationProviderManager<Product, BigDecimal>
            specificationProviderManagerBigDec;
    private final SpecificationProviderManager<Product, String>
            specificationProviderManagerString;

    @Override
    public Specification<Product> build(ProductSearchDto requestDto) {
        Specification<Product> specs = Specification.where(null);

        if (requestDto.getName() != null
                && !requestDto.getName().isEmpty()) {
            specs = specs.and(
                    specificationProviderManagerString.getSpecificationProvider("name")
                    .getSpecification(requestDto.getName())
            );
        }
        if (requestDto.getUniqCode() != null
                && !requestDto.getUniqCode().isEmpty()) {
            specs = specs.and(
                    specificationProviderManagerString.getSpecificationProvider("uniqCode")
                            .getSpecification(requestDto.getUniqCode())
            );
        }
        if (requestDto.getWholesalePriceMin() != null) {
            specs = specs.and(
                    specificationProviderManagerBigDec.getSpecificationProvider("wholesalePriceMin")
                    .getSpecification(requestDto.getWholesalePriceMin())
            );
        }
        if (requestDto.getWholesalePriceMax() != null) {
            specs = specs.and(
                    specificationProviderManagerBigDec.getSpecificationProvider("wholesalePriceMax")
                    .getSpecification(requestDto.getWholesalePriceMax())
            );
        }
        if (requestDto.getRetailPriceMin() != null) {
            specs = specs.and(
                    specificationProviderManagerBigDec.getSpecificationProvider("retailPriceMin")
                            .getSpecification(requestDto.getRetailPriceMin())
            );
        }
        if (requestDto.getRetailPriceMax() != null) {
            specs = specs.and(
                    specificationProviderManagerBigDec.getSpecificationProvider("retailPriceMax")
                            .getSpecification(requestDto.getRetailPriceMax())
            );
        }
        if (requestDto.getCategoryIds() != null) {
            specs = specs.and(
                    specificationProviderManagerLongArr.getSpecificationProvider("categoryIds")
                            .getSpecification(requestDto.getCategoryIds())
            );
        }
        return specs;
    }
}
