package project.inventorymanager.repositoryservice;

import project.inventorymanager.model.user.User;

public interface UserRepositoryService {
    User getByEmail(String email);

    User getById(Long id);

    User save(User user);

    void isAlreadyExist(String email);
}
