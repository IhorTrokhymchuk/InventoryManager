package project.inventorymanager.repository.product;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.inventorymanager.model.product.Product;
import project.inventorymanager.repository.specefication.SpecificationProvider;
import project.inventorymanager.repository.specefication.SpecificationProviderManager;

@RequiredArgsConstructor
@Component
public class ProductSpecificationProviderManagerString
        implements SpecificationProviderManager<Product, String> {
    private final List<SpecificationProvider<Product, String>>
            specificationProvidersLongArr;

    @Override
    public SpecificationProvider<Product, String> getSpecificationProvider(String key) {
        return specificationProvidersLongArr.stream()
            .filter(spec -> spec.getKey().equals(key))
            .findFirst()
            .orElseThrow(
                    () -> new RuntimeException("Can't find correct "
                        + "accommodationSpecificationProvider where key = " + key));
    }
}
