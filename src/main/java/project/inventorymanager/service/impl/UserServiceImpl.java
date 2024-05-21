package project.inventorymanager.service.impl;

import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.inventorymanager.dto.user.request.UserRegistrationRequestDto;
import project.inventorymanager.dto.user.request.UserUpdateInfoRequestDto;
import project.inventorymanager.dto.user.request.UserUpdatePasswordRequestDto;
import project.inventorymanager.dto.user.request.UserUpdateRolesRequestDto;
import project.inventorymanager.dto.user.response.UserResponseDto;
import project.inventorymanager.exception.auth.PasswordNotValidException;
import project.inventorymanager.exception.repository.EntityAlreadyExistsException;
import project.inventorymanager.exception.repository.EntityNotFoundException;
import project.inventorymanager.mapper.UserMapper;
import project.inventorymanager.model.user.RoleType;
import project.inventorymanager.model.user.User;
import project.inventorymanager.repository.RoleTypeRepository;
import project.inventorymanager.repository.UserRepository;
import project.inventorymanager.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final RoleType.RoleName CUSTOMER_ROLE_TYPE = RoleType.RoleName.USER;
    private final RoleTypeRepository roleTypeRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserResponseDto getInfo(String email) {
        return userMapper.toResponseDto(getUser(email));
    }

    @Override
    @Transactional
    public UserResponseDto updateInfo(String email, UserUpdateInfoRequestDto requestDto) {
        User user = getUser(email);
        setUserInfo(user, requestDto);
        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void updatePassword(String email, UserUpdatePasswordRequestDto requestDto) {
        User user = getUser(email);
        if (!passwordEncoder.matches(requestDto.getOldPassword(), user.getPassword())) {
            throw new PasswordNotValidException("Old password dont valid");
        }
        isPasswordsValid(requestDto.getNewPassword(), requestDto.getRepeatNewPassword());
        setPassword(user, requestDto.getNewPassword());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserResponseDto updateRoles(Long id, UserUpdateRolesRequestDto requestDto) {
        User user = getUser(id);
        setRoleType(user, requestDto.getRoleName());
        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        isUserAlreadyExist(requestDto.getEmail());
        User newUser = userMapper.toModelWithoutPasswordAndRoles(requestDto);
        isPasswordsValid(requestDto.getPassword(), requestDto.getRepeatPassword());
        setPassword(newUser, requestDto.getPassword());
        setRoleType(newUser, CUSTOMER_ROLE_TYPE);
        return userMapper.toResponseDto(userRepository.save(newUser));
    }

    private void setUserInfo(User user, UserUpdateInfoRequestDto requestDto) {
        isUserAlreadyExist(requestDto.getEmail());
        userMapper.setUpdateInfoToUser(user, requestDto);
    }

    private void isUserAlreadyExist(String email) {
        Optional<User> userByEmail = userRepository.findUserByEmail(email);
        if (userByEmail.isPresent()) {
            throw new EntityAlreadyExistsException("User with email: " + email + " is exist");
        }
    }

    private User getUser(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Cant find user with email: " + email));
    }

    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cant find user with id: " + id));
    }

    private void setPassword(User user, String password) {
        String encodePassword = passwordEncoder.encode(password);
        user.setPassword(encodePassword);
    }

    private void isPasswordsValid(String password, String repeatPassword) {
        if (!password.equals(repeatPassword)) {
            throw new PasswordNotValidException("Passwords is different");
        }
    }

    private void setRoleType(User user, RoleType.RoleName highestRole) {
        List<RoleType.RoleName> roleNamesSubList = RoleType.RoleName.getRolesUpTo(highestRole);;
        List<RoleType> roleTypes = roleTypeRepository.findRoleTypesByNameIn(roleNamesSubList);
        Set<RoleType> newRoleTypes = new HashSet<>(roleTypes);
        user.setRoles(newRoleTypes);
    }
}
