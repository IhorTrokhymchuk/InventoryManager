package project.inventorymanager.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import project.inventorymanager.model.inventoryaction.InventoryActionType;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class InventoryActionTypeRepositoryTest {
    @Autowired
    private InventoryActionTypeRepository inventoryActionTypeRepository;

    @Test
    @DisplayName("Find InventoryActionType by InventoryActionTypeName with exist data")
    void findInventoryActionTypeByName_findByExistName_inventoryActionType() {
        InventoryActionType.InventoryActionTypeName name
                = InventoryActionType.InventoryActionTypeName.REPLENISHMENT;
        InventoryActionType actualActionType
                = inventoryActionTypeRepository.findInventoryActionTypeByName(name);
        assertEquals(name, actualActionType.getName());
    }
}
