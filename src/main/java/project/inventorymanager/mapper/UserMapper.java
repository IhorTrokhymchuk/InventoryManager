package project.inventorymanager.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import project.inventorymanager.config.MapperConfig;
import project.inventorymanager.dto.user.request.UserRegistrationRequestDto;
import project.inventorymanager.dto.user.request.UserUpdateInfoRequestDto;
import project.inventorymanager.dto.user.response.UserResponseDto;
import project.inventorymanager.model.user.RoleType;
import project.inventorymanager.model.user.User;

@Mapper(config = MapperConfig.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    @Mapping(target = "roleNames", ignore = true)
    UserResponseDto toResponseDto(User user);

    @AfterMapping
    default void setRoleNames(@MappingTarget UserResponseDto responseDto, User user) {
        Set<String> roleNames = user.getRoles().stream()
                .map(RoleType::getName)
                .map(Enum::toString)
                .collect(Collectors.toSet());
        responseDto.setRoleNames(roleNames);
    }

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User toModelWithoutPasswordAndRoles(UserRegistrationRequestDto requestDto);

    void setUpdateInfoToUser(@MappingTarget User user, UserUpdateInfoRequestDto requestDto);
}
