package project.inventorymanager.dto.user.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import project.inventorymanager.validation.EmailValues;
import project.inventorymanager.validation.PasswordValues;

@Data
public class UserLoginRequestDto {
    @EmailValues
    @NotNull
    private String email;
    @PasswordValues
    private String password;
}
