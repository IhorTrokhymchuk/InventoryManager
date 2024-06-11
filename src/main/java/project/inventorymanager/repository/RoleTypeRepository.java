package project.inventorymanager.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import project.inventorymanager.model.user.RoleType;

public interface RoleTypeRepository extends JpaRepository<RoleType, Long> {
    List<RoleType> findRoleTypesByNameIn(List<RoleType.RoleName> roleNames);
}
