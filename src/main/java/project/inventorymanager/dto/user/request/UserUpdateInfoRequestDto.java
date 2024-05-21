package project.inventorymanager.dto.user.request;

import lombok.Data;
import project.inventorymanager.validation.EmailValues;

@Data
public class UserUpdateInfoRequestDto {
    @EmailValues
    private String email;
    private String firstName;
    private String lastName;
}
