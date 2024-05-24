package project.inventorymanager.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import project.inventorymanager.dto.inventoryaction.request.InventoryActionRequestDto;
import project.inventorymanager.dto.inventoryaction.response.InventoryActionResponseDto;

public interface InventoryActionService {
    InventoryActionResponseDto save(InventoryActionRequestDto requestDto, String email);

    InventoryActionResponseDto getById(Long id, String email);

    List<InventoryActionResponseDto> findAll(Pageable pageable, String email);
}
