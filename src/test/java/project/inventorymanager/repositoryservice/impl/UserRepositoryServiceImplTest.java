package project.inventorymanager.repositoryservice.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import project.inventorymanager.exception.repository.EntityAlreadyExistsException;
import project.inventorymanager.exception.repository.EntityNotFoundException;
import project.inventorymanager.model.user.User;
import project.inventorymanager.repository.UserRepository;

@ExtendWith(SpringExtension.class)
public class UserRepositoryServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserRepositoryServiceImpl userRepositoryService;

    @Test
    @DisplayName("Test get user by email successfully")
    public void testGetByEmail() {
        String email = "test@example.com";
        User user = new User();
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));

        User foundUser = userRepositoryService.getByEmail(email);

        assertNotNull(foundUser);
        verify(userRepository, times(1)).findUserByEmail(email);
    }

    @Test
    @DisplayName("Test get user by email throws EntityNotFoundException")
    public void testGetByEmailNotFound() {
        String email = "test@example.com";
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                userRepositoryService.getByEmail(email));

        assertEquals("Cant find user with email: " + email, exception.getMessage());
        verify(userRepository, times(1)).findUserByEmail(email);
    }

    @Test
    @DisplayName("Test get user by id successfully")
    public void testGetById() {
        Long id = 1L;
        User user = new User();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User foundUser = userRepositoryService.getById(id);

        assertNotNull(foundUser);
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Test get user by id throws EntityNotFoundException")
    public void testGetByIdNotFound() {
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                userRepositoryService.getById(id));

        assertEquals("Cant find user with id: " + id, exception.getMessage());
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Test save user successfully")
    public void testSaveUser() {
        User user = new User();
        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userRepositoryService.save(user);

        assertNotNull(savedUser);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Test check if user already exists by email throws EntityAlreadyExistsException")
    public void testIsAlreadyExist() {
        String email = "test@example.com";
        User user = new User();
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));

        EntityAlreadyExistsException exception = assertThrows(
                EntityAlreadyExistsException.class, () ->
                        userRepositoryService.isAlreadyExist(email));

        assertEquals("User with email: " + email + " is exist", exception.getMessage());
        verify(userRepository, times(1)).findUserByEmail(email);
    }

    @Test
    @DisplayName("Test check if user already exists by email and does not throw exception")
    public void testIsAlreadyExistNotFound() {
        String email = "test@example.com";
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> userRepositoryService.isAlreadyExist(email));
        verify(userRepository, times(1)).findUserByEmail(email);
    }
}
