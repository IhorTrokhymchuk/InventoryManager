package project.inventorymanager.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import project.inventorymanager.dto.inventory.response.InventoryResponseDto;

public interface InventoryService {
    List<InventoryResponseDto> findAll(Pageable pageable, String email);
}
