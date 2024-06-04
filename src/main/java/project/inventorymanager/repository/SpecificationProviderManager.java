package project.inventorymanager.repository;

public interface SpecificationProviderManager<T, D> {
    SpecificationProvider<T, D> getSpecificationProvider(String key);
}
