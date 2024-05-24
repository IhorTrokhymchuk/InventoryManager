package project.inventorymanager.repositoryservice.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.inventorymanager.exception.repository.EntityNotFoundException;
import project.inventorymanager.model.inventoryaction.InventoryActionType;
import project.inventorymanager.repository.InventoryActionTypeRepository;
import project.inventorymanager.repositoryservice.InventoryActionTypeRepoService;

@Service
@RequiredArgsConstructor
public class InventoryActionTypeRepoServiceImpl implements InventoryActionTypeRepoService {
    private final InventoryActionTypeRepository inventoryActionTypeRepository;

    @Override
    public InventoryActionType save(InventoryActionType inventoryActionType) {
        return inventoryActionTypeRepository.save(inventoryActionType);
    }

    @Override
    public InventoryActionType getById(Long id) {
        return inventoryActionTypeRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        "Cant find inventory action type with id: " + id));
    }

    @Override
    public InventoryActionType getByTypeName(InventoryActionType.InventoryActionTypeName name) {
        return inventoryActionTypeRepository.findInventoryActionTypeByName(name);
    }
}
