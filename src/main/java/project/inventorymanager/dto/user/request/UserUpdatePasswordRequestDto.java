package project.inventorymanager.dto.user.request;

import lombok.Data;
import project.inventorymanager.validation.PasswordValues;

@Data
public class UserUpdatePasswordRequestDto {
    @PasswordValues
    private String oldPassword;
    @PasswordValues
    private String newPassword;
    @PasswordValues
    private String repeatNewPassword;
}
