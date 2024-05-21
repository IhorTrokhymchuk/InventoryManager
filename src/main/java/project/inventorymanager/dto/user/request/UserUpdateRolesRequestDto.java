package project.inventorymanager.dto.user.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import project.inventorymanager.model.user.RoleType;

@Data
public class UserUpdateRolesRequestDto {
    @NotNull
    private RoleType.RoleName roleName;
}
