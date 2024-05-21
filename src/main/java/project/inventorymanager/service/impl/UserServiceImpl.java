package project.inventorymanager.service.impl;

import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
import project.inventorymanager.repositoryservice.UserRepoService;
import project.inventorymanager.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final RoleType.RoleName CUSTOMER_ROLE_TYPE = RoleType.RoleName.USER;
    private final RoleTypeRepository roleTypeRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserRepoService userRepoService;

    @Override
    @Transactional
    public UserResponseDto getInfo(String email) {
        return userMapper.toResponseDto(userRepoService.getByEmail(email));
    }

    @Override
    @Transactional
    public UserResponseDto updateInfo(String email, UserUpdateInfoRequestDto requestDto) {
        User user = userRepoService.getByEmail(email);
        setUserInfo(user, requestDto);
        return userMapper.toResponseDto(userRepoService.save(user));
    }

    private void setUserInfo(User user, UserUpdateInfoRequestDto requestDto) {
        userRepoService.isAlreadyExist(requestDto.getEmail());
        userMapper.setUpdateInfoToUser(user, requestDto);
    }

    @Override
    @Transactional
    public void updatePassword(String email, UserUpdatePasswordRequestDto requestDto) {
        User user = userRepoService.getByEmail(email);
        if (!passwordEncoder.matches(requestDto.getOldPassword(), user.getPassword())) {
            throw new PasswordNotValidException("Old password dont valid");
        }
        isPasswordsValid(requestDto.getNewPassword(), requestDto.getRepeatNewPassword());
        setPassword(user, requestDto.getNewPassword());
        userRepoService.save(user);
    }

    private void isPasswordsValid(String password, String repeatPassword) {
        if (!password.equals(repeatPassword)) {
            throw new PasswordNotValidException("Passwords is different");
        }
    }

    private void setPassword(User user, String password) {
        String encodePassword = passwordEncoder.encode(password);
        user.setPassword(encodePassword);
    }

    @Override
    @Transactional
    public UserResponseDto updateRoles(Long id, UserUpdateRolesRequestDto requestDto) {
        User user = userRepoService.getById(id);
        setRoleType(user, requestDto.getRoleName());
        return userMapper.toResponseDto(userRepoService.save(user));
    }

    private void setRoleType(User user, RoleType.RoleName highestRole) {
        List<RoleType.RoleName> roleNamesSubList = RoleType.RoleName.getRolesUpTo(highestRole);;
        List<RoleType> roleTypes = roleTypeRepository.findRoleTypesByNameIn(roleNamesSubList);
        Set<RoleType> newRoleTypes = new HashSet<>(roleTypes);
        user.setRoles(newRoleTypes);
    }

    @Override
    @Transactional
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        userRepoService.isAlreadyExist(requestDto.getEmail());
        User user = userMapper.toModelWithoutPasswordAndRoles(requestDto);
        isPasswordsValid(requestDto.getPassword(), requestDto.getRepeatPassword());
        setPassword(user, requestDto.getPassword());
        setRoleType(user, CUSTOMER_ROLE_TYPE);
        return userMapper.toResponseDto(userRepoService.save(user));
    }
}
