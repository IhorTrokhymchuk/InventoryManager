package project.inventorymanager.exception.user;

public class UserDontHavePermissions extends RuntimeException {
    public UserDontHavePermissions(String message) {
        super(message);
    }
}
