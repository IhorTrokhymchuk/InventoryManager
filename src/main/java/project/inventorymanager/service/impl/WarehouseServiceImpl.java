package project.inventorymanager.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.inventorymanager.dto.warehouse.request.WarehouseRequestDto;
import project.inventorymanager.dto.warehouse.response.WarehouseResponseDto;
import project.inventorymanager.mapper.WarehouseMapper;
import project.inventorymanager.model.user.User;
import project.inventorymanager.model.warehouse.Warehouse;
import project.inventorymanager.repositoryservice.UserRepoService;
import project.inventorymanager.repositoryservice.WarehouseRepoService;
import project.inventorymanager.service.WarehouseService;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final UserRepoService userRepoService;
    private final WarehouseRepoService warehouseRepoService;
    private final WarehouseMapper warehouseMapper;

    @Override
    public WarehouseResponseDto save(WarehouseRequestDto requestDto, String email) {
        Warehouse warehouse = warehouseMapper.toModelWithoutUser(requestDto);
        User user = userRepoService.getByEmail(email);
        warehouse.setUser(user);
        return warehouseMapper.toResponseDto(warehouseRepoService.save(warehouse));
    }

    @Override
    public WarehouseResponseDto getById(Long id, String email) {
        return warehouseMapper.toResponseDto(
                warehouseRepoService.getByIdIfUserHavePermission(id, email));
    }

    @Override
    public List<WarehouseResponseDto> findAll(Pageable pageable, String email) {
        return warehouseRepoService.findAllByUserEmail(pageable, email).stream()
                .map(warehouseMapper::toResponseDto)
                .toList();
    }

    @Override
    public void deleteById(Long id, String email) {
        warehouseRepoService.deleteByIdIfUserHavePermission(id, email);
    }
}
