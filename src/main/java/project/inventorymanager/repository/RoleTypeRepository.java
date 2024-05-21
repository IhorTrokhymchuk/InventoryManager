package project.inventorymanager.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import project.inventorymanager.model.user.RoleType;

public interface RoleTypeRepository extends JpaRepository<RoleType, Long> {
    RoleType findRoleTypeByName(RoleType.RoleName roleName);

    List<RoleType> findRoleTypesByNameIn(List<RoleType.RoleName> roleNames);
}
