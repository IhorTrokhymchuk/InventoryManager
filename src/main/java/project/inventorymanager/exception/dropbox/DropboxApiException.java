package project.inventorymanager.exception.dropbox;

public class DropboxApiException extends RuntimeException {
    public DropboxApiException(String message) {
        super(message);
    }
}
