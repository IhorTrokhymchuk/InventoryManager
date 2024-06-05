package project.inventorymanager.repository.specefication;

public interface SpecificationProviderManager<T, D> {
    SpecificationProvider<T, D> getSpecificationProvider(String key);
}
