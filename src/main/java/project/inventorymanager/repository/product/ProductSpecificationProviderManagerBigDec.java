package project.inventorymanager.repository.product;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.inventorymanager.model.product.Product;
import project.inventorymanager.repository.SpecificationProvider;
import project.inventorymanager.repository.SpecificationProviderManager;

@RequiredArgsConstructor
@Component
public class ProductSpecificationProviderManagerBigDec
        implements SpecificationProviderManager<Product, BigDecimal> {
    private final List<SpecificationProvider<Product, BigDecimal>>
            specificationProvidersLongArr;

    @Override
    public SpecificationProvider<Product, BigDecimal> getSpecificationProvider(String key) {
        return specificationProvidersLongArr.stream()
            .filter(spec -> spec.getKey().equals(key))
            .findFirst()
            .orElseThrow(
                    () -> new RuntimeException("Can't find correct "
                        + "accommodationSpecificationProvider where key = " + key));
    }
}
