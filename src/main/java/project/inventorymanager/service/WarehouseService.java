package project.inventorymanager.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import project.inventorymanager.dto.warehouse.request.WarehouseRequestDto;
import project.inventorymanager.dto.warehouse.response.WarehouseResponseDto;

public interface WarehouseService {
    WarehouseResponseDto save(WarehouseRequestDto requestDto);

    WarehouseResponseDto getById(Long id);

    List<WarehouseResponseDto> findAll(Pageable pageable);

    void deleteById(Long id);
}
