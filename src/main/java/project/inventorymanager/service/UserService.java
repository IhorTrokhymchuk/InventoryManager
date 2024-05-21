package project.inventorymanager.service;

import project.inventorymanager.dto.user.request.UserRegistrationRequestDto;
import project.inventorymanager.dto.user.request.UserUpdateInfoRequestDto;
import project.inventorymanager.dto.user.request.UserUpdatePasswordRequestDto;
import project.inventorymanager.dto.user.request.UserUpdateRolesRequestDto;
import project.inventorymanager.dto.user.response.UserResponseDto;

public interface UserService {
    UserResponseDto getInfo(String email);

    UserResponseDto updateInfo(String email, UserUpdateInfoRequestDto requestDto);

    void updatePassword(String email, UserUpdatePasswordRequestDto requestDto);

    UserResponseDto updateRoles(Long id, UserUpdateRolesRequestDto requestDto);

    UserResponseDto register(UserRegistrationRequestDto requestDto);
}
