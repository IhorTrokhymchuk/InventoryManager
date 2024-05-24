package project.inventorymanager.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import project.inventorymanager.dto.warehouse.request.WarehouseRequestDto;
import project.inventorymanager.dto.warehouse.response.WarehouseResponseDto;

public interface WarehouseService {
    WarehouseResponseDto save(WarehouseRequestDto requestDto, String email);

    WarehouseResponseDto getById(Long id, String email);

    List<WarehouseResponseDto> findAll(Pageable pageable, String email);

    void deleteById(Long id, String email);
}
