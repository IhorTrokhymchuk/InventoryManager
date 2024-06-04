package project.inventorymanager.repository.product;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.inventorymanager.model.product.Product;
import project.inventorymanager.repository.SpecificationProvider;
import project.inventorymanager.repository.SpecificationProviderManager;

@RequiredArgsConstructor
@Component
public class ProductSpecificationProviderManagerLongArr
        implements SpecificationProviderManager<Product, Long[]> {
    private final List<SpecificationProvider<Product, Long[]>> specificationProvidersLongArr;

    @Override
    public SpecificationProvider<Product, Long[]> getSpecificationProvider(String key) {
        return specificationProvidersLongArr.stream()
            .filter(spec -> spec.getKey().equals(key))
            .findFirst()
            .orElseThrow(
                    () -> new RuntimeException("Can't find correct "
                        + "ProductSpecificationProvider where key = " + key));
    }
}
