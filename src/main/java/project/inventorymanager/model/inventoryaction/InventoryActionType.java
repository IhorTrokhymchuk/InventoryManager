package project.inventorymanager.model.inventoryaction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "inventory_action_types")
public class InventoryActionType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "varchar", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private InventoryActionTypeName name;

    public enum InventoryActionTypeName {
        SHIPMENT,
        REPLENISHMENT,
    }
}
