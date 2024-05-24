package project.inventorymanager.repositoryservice.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.inventorymanager.exception.repository.EntityAlreadyExistsException;
import project.inventorymanager.exception.repository.EntityNotFoundException;
import project.inventorymanager.model.user.User;
import project.inventorymanager.repository.UserRepository;
import project.inventorymanager.repositoryservice.UserRepoService;

@Service
@RequiredArgsConstructor
public class UserRepoServiceImpl implements UserRepoService {
    private final UserRepository userRepository;

    @Override
    public User getByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Cant find user with email: " + email));
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Cant find user with id: " + id));
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void isAlreadyExist(String email) {
        Optional<User> userByEmail = userRepository.findUserByEmail(email);
        if (userByEmail.isPresent()) {
            throw new EntityAlreadyExistsException("User with email: " + email + " is exist");
        }
    }
}
