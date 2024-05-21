package project.inventorymanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.inventorymanager.dto.user.request.UserUpdateInfoRequestDto;
import project.inventorymanager.dto.user.request.UserUpdatePasswordRequestDto;
import project.inventorymanager.dto.user.request.UserUpdateRolesRequestDto;
import project.inventorymanager.dto.user.response.UserResponseDto;
import project.inventorymanager.service.UserService;

@Tag(name = "User management", description = "Endpoints manage user info")
@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Get info about user",
            description = "Get all info about user")
    public UserResponseDto getInfo(Authentication authentication) {
        return userService.getInfo(authentication.getName());
    }

    @PatchMapping("/me")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Update info about user",
            description = "Update email, first and last names user")
    public UserResponseDto updateInfo(
            Authentication authentication,
            @RequestBody @Valid UserUpdateInfoRequestDto requestDto) {
        return userService.updateInfo(authentication.getName(), requestDto);
    }

    @PutMapping("/me/password")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Update password",
            description = "Update user password")
    public void updatePassword(
            Authentication authentication,
            @RequestBody @Valid UserUpdatePasswordRequestDto requestDto) {
        userService.updatePassword(authentication.getName(), requestDto);
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update accommodation by id",
            description = "Update existing accommodation field by id")
    public UserResponseDto updateRoles(@PathVariable Long id,
                                       @RequestBody @Valid UserUpdateRolesRequestDto requestDto) {
        return userService.updateRoles(id, requestDto);
    }
}
