package project.inventorymanager.testutil.objects;

import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Component;
import project.inventorymanager.dto.user.request.UserRegistrationRequestDto;
import project.inventorymanager.dto.user.request.UserUpdateInfoRequestDto;
import project.inventorymanager.dto.user.request.UserUpdatePasswordRequestDto;
import project.inventorymanager.dto.user.response.UserResponseDto;
import project.inventorymanager.model.user.RoleType;
import project.inventorymanager.model.user.User;

@Component
public class UserProvider {
    public static final String MAIL = "test@example.com";
    public static final String FIRST_NAME = "John";
    public static final String LAST_NAME = "Doe";
    public static final String UPD_FIRST_NAME = "JohnUpd";
    public static final String UPD_LAST_NAME = "DoeUpd";
    public static final String UPD_MAIL = "testupd@example.com";
    public static final String PASSWORD = "Test@200@test";
    public static final String UPD_PASSWORD = "newPassword";
    public static final String HASH_PASSWORD
            = "$2a$10$jQuBxj/mnQTEY60FgHeYiu8SahQYuj0O8shQtDMIAOAC1kYH3/2/6";

    public static UserResponseDto getUserResponseDto(Long param) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setEmail(MAIL + param);
        userResponseDto.setFirstName(FIRST_NAME + param);
        userResponseDto.setLastName(LAST_NAME + param);
        userResponseDto.setRoleNames(new HashSet<>(Set.of(RoleType.RoleName.USER.name())));
        return userResponseDto;
    }

    public static UserRegistrationRequestDto getUserRegistrationRequestDto(Long param) {
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto();
        requestDto.setEmail(MAIL + param);
        requestDto.setFirstName(FIRST_NAME + param);
        requestDto.setLastName(LAST_NAME + param);
        requestDto.setPassword(PASSWORD);
        requestDto.setRepeatPassword(PASSWORD);
        return requestDto;
    }

    public static UserUpdateInfoRequestDto getUserUpdateInfoRequestDto(Long param) {
        UserUpdateInfoRequestDto userResponseDto = new UserUpdateInfoRequestDto();
        userResponseDto.setEmail(UPD_MAIL + param);
        userResponseDto.setFirstName(UPD_FIRST_NAME + param);
        userResponseDto.setLastName(UPD_LAST_NAME + param);
        return userResponseDto;
    }

    public static UserUpdatePasswordRequestDto getUserUpdatePasswordRequestDto(
            String oldPassword) {
        UserUpdatePasswordRequestDto requestDto = new UserUpdatePasswordRequestDto();
        requestDto.setOldPassword(oldPassword);
        requestDto.setNewPassword(UPD_PASSWORD);
        requestDto.setRepeatNewPassword(UPD_PASSWORD);
        return requestDto;
    }

    public static User getUser(Long id) {
        User user = new User();
        user.setId(id);
        user.setEmail(MAIL + id);
        user.setFirstName(FIRST_NAME + id);
        user.setLastName(LAST_NAME + id);
        user.setPassword(HASH_PASSWORD);
        user.setRoles(new HashSet<>(Set.of(getUserRoleType())));
        return user;
    }

    public static RoleType getUserRoleType() {
        RoleType roleType = new RoleType();
        roleType.setName(RoleType.RoleName.USER);
        return roleType;
    }
}
