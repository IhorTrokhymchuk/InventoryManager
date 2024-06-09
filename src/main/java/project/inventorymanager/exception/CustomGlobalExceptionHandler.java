package project.inventorymanager.exception;

import io.jsonwebtoken.ExpiredJwtException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import project.inventorymanager.exception.dropbox.DropboxApiException;
import project.inventorymanager.exception.file.WorkWithFileExceptions;
import project.inventorymanager.exception.inventory.InventoryQuantityException;
import project.inventorymanager.exception.repository.EntityAlreadyExistsException;
import project.inventorymanager.exception.repository.EntityNotFoundException;
import project.inventorymanager.exception.user.PasswordNotValidException;
import project.inventorymanager.exception.user.UserDontHavePermissions;
import project.inventorymanager.exception.warehouse.WarehouseDontHaveFreeCapacityException;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final int BAD_REQUEST_STATUS_CODE = 400;
    private static final int UNAUTHORIZED_STATUS_CODE = 401;
    private static final int FORBIDDEN_STATUS_CODE = 403;
    private static final int NOT_FOUND_STATUS_CODE = 404;
    private static final int CONFLICT_STATUS_CODE = 409;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        Map<String, String> errorsBody = new LinkedHashMap<>();
        errorsBody.put("timestamp", String.valueOf(LocalDateTime.now()));
        errorsBody.put("status", String.valueOf(HttpStatusCode.valueOf(BAD_REQUEST_STATUS_CODE)));
        final List<String> messagesList = ex.getBindingResult().getAllErrors().stream()
                .map(this::getErrorMessage)
                .toList();
        errorsBody.put("errors", messagesList.toString());
        return new ResponseEntity<>(errorsBody, status);
    }

    private String getErrorMessage(ObjectError e) {
        if (e instanceof FieldError) {
            String field = ((FieldError) e).getField();
            String message = e.getDefaultMessage();
            return field + " " + message;
        }
        return e.getDefaultMessage();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleCustomException(EntityNotFoundException ex) {
        return getObjectResponseEntity(ex.getMessage(),
                HttpStatusCode.valueOf(NOT_FOUND_STATUS_CODE));
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<Object> handleCustomException(EntityAlreadyExistsException ex) {
        return getObjectResponseEntity(ex.getMessage(),
                HttpStatusCode.valueOf(CONFLICT_STATUS_CODE));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleCustomException(ExpiredJwtException ex) {
        return getObjectResponseEntity(ex.getMessage(),
                HttpStatusCode.valueOf(UNAUTHORIZED_STATUS_CODE));
    }

    @ExceptionHandler(PasswordNotValidException.class)
    public ResponseEntity<Object> handleCustomException(PasswordNotValidException ex) {
        return getObjectResponseEntity(ex.getMessage(),
                HttpStatusCode.valueOf(UNAUTHORIZED_STATUS_CODE));
    }

    @ExceptionHandler(UserDontHavePermissions.class)
    public ResponseEntity<Object> handleCustomException(UserDontHavePermissions ex) {
        return getObjectResponseEntity(ex.getMessage(),
                HttpStatusCode.valueOf(FORBIDDEN_STATUS_CODE));
    }

    @ExceptionHandler(WarehouseDontHaveFreeCapacityException.class)
    public ResponseEntity<Object> handleCustomException(WarehouseDontHaveFreeCapacityException ex) {
        return getObjectResponseEntity(ex.getMessage(),
                HttpStatusCode.valueOf(CONFLICT_STATUS_CODE));
    }

    @ExceptionHandler(InventoryQuantityException.class)
    public ResponseEntity<Object> handleCustomException(InventoryQuantityException ex) {
        return getObjectResponseEntity(ex.getMessage(),
                HttpStatusCode.valueOf(CONFLICT_STATUS_CODE));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleCustomException(IllegalArgumentException ex) {
        return getObjectResponseEntity(ex.getMessage(),
                HttpStatusCode.valueOf(CONFLICT_STATUS_CODE));
    }

    @ExceptionHandler(WorkWithFileExceptions.class)
    public ResponseEntity<Object> handleCustomException(WorkWithFileExceptions ex) {
        return getObjectResponseEntity(ex.getMessage(),
                HttpStatusCode.valueOf(CONFLICT_STATUS_CODE));
    }

    @ExceptionHandler(DropboxApiException.class)
    public ResponseEntity<Object> handleCustomException(DropboxApiException ex) {
        return getObjectResponseEntity(ex.getMessage(),
                HttpStatusCode.valueOf(CONFLICT_STATUS_CODE));
    }

    private ResponseEntity<Object> getObjectResponseEntity(String message,
                                                           HttpStatusCode httpStatusCode) {
        Map<String, String> errorsBody = new LinkedHashMap<>();
        errorsBody.put("timestamp", String.valueOf(LocalDateTime.now()));
        errorsBody.put("status", String.valueOf(httpStatusCode));
        errorsBody.put("errors", message);
        return new ResponseEntity<>(errorsBody, httpStatusCode);
    }
}
