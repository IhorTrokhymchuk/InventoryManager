package project.inventorymanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static project.inventorymanager.testutil.objects.UserProvider.getUser;
import static project.inventorymanager.testutil.objects.UserProvider.getUserRegistrationRequestDto;
import static project.inventorymanager.testutil.objects.UserProvider.getUserResponseDto;
import static project.inventorymanager.testutil.objects.UserProvider.getUserRoleType;
import static project.inventorymanager.testutil.objects.UserProvider.getUserUpdateInfoRequestDto;
import static project.inventorymanager.testutil.objects.UserProvider.getUserUpdatePasswordRequestDto;

import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.inventorymanager.dto.user.request.UserRegistrationRequestDto;
import project.inventorymanager.dto.user.request.UserUpdateInfoRequestDto;
import project.inventorymanager.dto.user.request.UserUpdatePasswordRequestDto;
import project.inventorymanager.dto.user.request.UserUpdateRolesRequestDto;
import project.inventorymanager.dto.user.response.UserResponseDto;
import project.inventorymanager.exception.user.PasswordNotValidException;
import project.inventorymanager.mapper.UserMapper;
import project.inventorymanager.model.user.RoleType;
import project.inventorymanager.model.user.User;
import project.inventorymanager.repository.RoleTypeRepository;
import project.inventorymanager.repositoryservice.UserRepositoryService;
import project.inventorymanager.service.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private static final RoleType.RoleName USER = RoleType.RoleName.USER;
    private static final RoleType.RoleName EMPLOYEE = RoleType.RoleName.EMPLOYEE;
    private static final RoleType.RoleName ADMIN = RoleType.RoleName.ADMIN;
    @Mock
    private RoleTypeRepository roleTypeRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserRepositoryService userRepositoryService;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Get info about user with valid data")
    void getInfo_getUserInfoWithValidData_ReturnUser() {
        Long id = 1L;
        User user = getUser(id);
        UserResponseDto userResponseDto = getUserResponseDto(id);

        when(userRepositoryService.getByEmail(user.getEmail())).thenReturn(user);
        when(userMapper.toResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto result = userService.getInfo(user.getEmail());

        assertNotNull(result);
        assertEquals(userResponseDto, result);

        verify(userRepositoryService, times(1)).getByEmail(user.getEmail());
        verify(userMapper, times(1)).toResponseDto(user);
        verifyNoMoreInteractions(userRepositoryService, userMapper);
    }

    @Test
    @DisplayName("Update info about user with valid data")
    void updateInfo_updateUserInfoWithValidData_ReturnUpdateUser() {
        Long id = 1L;
        UserUpdateInfoRequestDto userUpdateInfoRequestDto = getUserUpdateInfoRequestDto(id);
        UserResponseDto userResponseDto = getUserResponseDto(id);

        userResponseDto.setEmail(userUpdateInfoRequestDto.getEmail());
        userResponseDto.setFirstName(userUpdateInfoRequestDto.getFirstName());
        userResponseDto.setLastName(userUpdateInfoRequestDto.getLastName());

        User user = getUser(id);
        final String oldEmail = user.getEmail();

        when(userRepositoryService.getByEmail(user.getEmail())).thenReturn(user);
        user.setFirstName(userUpdateInfoRequestDto.getFirstName());
        user.setLastName(userUpdateInfoRequestDto.getLastName());
        user.setEmail(userUpdateInfoRequestDto.getEmail());
        when(userRepositoryService.save(user)).thenReturn(user);
        when(userMapper.toResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto result = userService.updateInfo(oldEmail, userUpdateInfoRequestDto);
        assertNotNull(result);
        assertEquals(userResponseDto, result);

        verify(userRepositoryService, times(1)).getByEmail(oldEmail);
        verify(userRepositoryService, times(1))
                .isAlreadyExistThrowException(userUpdateInfoRequestDto.getEmail());
        verify(userMapper,times(1)).setUpdateInfoToUser(user, userUpdateInfoRequestDto);
        verify(userRepositoryService, times(1)).save(user);
        verify(userMapper, times(1)).toResponseDto(user);

        verifyNoMoreInteractions(userRepositoryService, userMapper);
    }

    @Test
    @DisplayName("Register user with valid data")
    void register_registerUserWithValidData_ReturnUser() {
        Long id = 1L;
        UserRegistrationRequestDto userRegistrationRequestDto = getUserRegistrationRequestDto(id);
        User user = getUser(id);

        when(userMapper.toModelWithoutPasswordAndRoles(userRegistrationRequestDto))
                .thenReturn(user);
        user.setPassword(userRegistrationRequestDto.getPassword());
        List<RoleType> roleTypeList = List.of(getUserRoleType());
        when(roleTypeRepository.findRoleTypesByNameIn(List.of(USER))).thenReturn(roleTypeList);
        user.setRoles(new HashSet<>(roleTypeList));
        when(userRepositoryService.save(user)).thenReturn(user);
        UserResponseDto userResponseDto = getUserResponseDto(1L);
        when(userMapper.toResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto result = userService.register(userRegistrationRequestDto);

        assertNotNull(result);
        assertEquals(userResponseDto, result);

        verify(userRepositoryService, times(1))
                .isAlreadyExistThrowException(userRegistrationRequestDto.getEmail());
        verify(userMapper,times(1)).toModelWithoutPasswordAndRoles(userRegistrationRequestDto);
        verify(roleTypeRepository, times(1)).findRoleTypesByNameIn(List.of(USER));
        verify(userRepositoryService, times(1)).save(user);
        verify(userMapper, times(1)).toResponseDto(user);
        verifyNoMoreInteractions(userRepositoryService, userMapper, roleTypeRepository);
    }

    @Test
    @DisplayName("Register user with non valid password")
    void register_registerUserWithNonValidPassword_ThrowException() {
        Long id = 1L;
        UserRegistrationRequestDto userRegistrationRequestDto = getUserRegistrationRequestDto(id);
        userRegistrationRequestDto.setRepeatPassword(
                userRegistrationRequestDto.getPassword() + "test");

        assertThrows(PasswordNotValidException.class,
                () -> userService.register(userRegistrationRequestDto));

        verify(userRepositoryService, times(1))
                .isAlreadyExistThrowException(userRegistrationRequestDto.getEmail());
        verifyNoMoreInteractions(userRepositoryService, userMapper, roleTypeRepository);
    }

    @Test
    @DisplayName("Update user password with valid data")
    void updatePassword_updateUserPasswordWithValidData_UpdateUserPassword() {
        Long id = 1L;
        User user = getUser(id);
        UserUpdatePasswordRequestDto userUpdatePasswordRequestDto
                = getUserUpdatePasswordRequestDto(user.getPassword());

        when(userRepositoryService.getByEmail(user.getEmail())).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        user.setPassword(userUpdatePasswordRequestDto.getNewPassword());
        when(userRepositoryService.save(user)).thenReturn(user);

        userService.updatePassword(user.getEmail(), userUpdatePasswordRequestDto);

        verify(userRepositoryService, times(1)).getByEmail(user.getEmail());
        verify(userRepositoryService, times(1)).save(user);
        verifyNoMoreInteractions(userRepositoryService, userMapper);
    }

    @Test
    @DisplayName("Update user password with non valid old password")
    void updatePassword_updateUserPasswordWithNotValidOldPassword_ThrowException() {
        Long id = 1L;
        User user = getUser(id);
        UserUpdatePasswordRequestDto userUpdatePasswordRequestDto
                = getUserUpdatePasswordRequestDto(user.getPassword() + "123");

        when(userRepositoryService.getByEmail(user.getEmail())).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(PasswordNotValidException.class,
                () -> userService.updatePassword(user.getEmail(), userUpdatePasswordRequestDto));

        verify(userRepositoryService, times(1)).getByEmail(user.getEmail());
        verifyNoMoreInteractions(userRepositoryService, userMapper);
    }

    @Test
    @DisplayName("Update user password with not equals new passwords")
    void updatePassword_updateUserPasswordWithNotValidNewPassword_ThrowException() {
        Long id = 1L;
        User user = getUser(id);
        UserUpdatePasswordRequestDto userUpdatePasswordRequestDto
                = getUserUpdatePasswordRequestDto(user.getPassword());
        userUpdatePasswordRequestDto.setRepeatNewPassword(
                userUpdatePasswordRequestDto.getNewPassword() + "123");

        when(userRepositoryService.getByEmail(user.getEmail())).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        assertThrows(PasswordNotValidException.class,
                () -> userService.updatePassword(user.getEmail(), userUpdatePasswordRequestDto));

        verify(userRepositoryService, times(1)).getByEmail(user.getEmail());
        verifyNoMoreInteractions(userRepositoryService, userMapper);
    }

    @Test
    @DisplayName("Update user roles with valid data")
    void updateRoles_updateUserRolesWithValidData_ReturnUser() {
        UserUpdateRolesRequestDto requestDto = new UserUpdateRolesRequestDto();
        requestDto.setRoleName(ADMIN);

        RoleType userRole = new RoleType();
        userRole.setName(USER);
        RoleType employeeRole = new RoleType();
        employeeRole.setName(EMPLOYEE);
        RoleType adminRole = new RoleType();
        adminRole.setName(ADMIN);

        Long id = 1L;
        List<String> roleTypeDtoList = List.of(USER.name(), EMPLOYEE.name(), ADMIN.name());
        UserResponseDto userResponseDto = getUserResponseDto(id);
        userResponseDto.setRoleNames(new HashSet<>(roleTypeDtoList));

        User user = getUser(id);

        when(userRepositoryService.getById(user.getId())).thenReturn(user);
        List<RoleType> roleTypeList = List.of(userRole, employeeRole, adminRole);
        List<RoleType.RoleName> roleNameList = List.of(USER, EMPLOYEE, ADMIN);

        when(roleTypeRepository.findRoleTypesByNameIn(roleNameList)).thenReturn(roleTypeList);

        user.setRoles(new HashSet<>(roleTypeList));
        when(userRepositoryService.save(user)).thenReturn(user);
        when(userMapper.toResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto result = userService.updateRoles(user.getId(), requestDto);

        assertNotNull(result);
        assertEquals(userResponseDto, result);

        verify(userRepositoryService, times(1)).getById(user.getId());
        verify(roleTypeRepository, times(1)). findRoleTypesByNameIn(roleNameList);
        verify(userRepositoryService, times(1)).save(user);
        verify(userMapper, times(1)).toResponseDto(user);
        verifyNoMoreInteractions(userRepositoryService, roleTypeRepository, userMapper);
    }
}
