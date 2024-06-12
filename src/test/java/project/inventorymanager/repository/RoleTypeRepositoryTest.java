package project.inventorymanager.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import project.inventorymanager.model.user.RoleType;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoleTypeRepositoryTest {
    private static final RoleType.RoleName USER_ROLE_NAME = RoleType.RoleName.USER;
    private static final RoleType.RoleName EMPLOYEE_ROLE_NAME = RoleType.RoleName.EMPLOYEE;
    private static final RoleType.RoleName ADMIN_ROLE_NAME = RoleType.RoleName.ADMIN;

    @Autowired
    private RoleTypeRepository roleTypeRepository;

    @Test
    @DisplayName("Find role types with exist data and one name")
    void findRoleTypesByNameIn_findWithOneNameInList_ReturnListWithOneRoleType() {
        List<RoleType.RoleName> roleNameList = List.of(USER_ROLE_NAME);
        List<RoleType> roleTypesByNameIn = roleTypeRepository.findRoleTypesByNameIn(roleNameList);
        assertEquals(1, roleTypesByNameIn.size());
        RoleType roleType = roleTypesByNameIn.get(0);
        assertEquals(USER_ROLE_NAME, roleType.getName());
    }

    @Test
    @DisplayName("Find role types with exist data and two name")
    void findRoleTypesByNameIn_findWithTwoNameInList_ReturnListWithTwoRoleType() {
        List<RoleType.RoleName> roleNameList = List.of(USER_ROLE_NAME, EMPLOYEE_ROLE_NAME);
        List<RoleType> roleTypesByNameIn = roleTypeRepository.findRoleTypesByNameIn(roleNameList);
        assertEquals(2, roleTypesByNameIn.size());
        List<RoleType.RoleName> roleNamesFromRepo = roleTypesByNameIn.stream()
                .map(RoleType::getName)
                .toList();
        assertTrue(roleNamesFromRepo.containsAll(roleNameList));
    }

    @Test
    @DisplayName("Find role types with exist data and two name")
    void findRoleTypesByNameIn_findWithThreeNameInList_ReturnListWithThreeRoleType() {
        List<RoleType.RoleName> roleNameList
                = List.of(USER_ROLE_NAME, EMPLOYEE_ROLE_NAME, ADMIN_ROLE_NAME);
        List<RoleType> roleTypesByNameIn = roleTypeRepository.findRoleTypesByNameIn(roleNameList);
        assertEquals(3, roleTypesByNameIn.size());
        List<RoleType.RoleName> roleNamesFromRepo = roleTypesByNameIn.stream()
                .map(RoleType::getName)
                .toList();
        assertTrue(roleNamesFromRepo.containsAll(roleNameList));
    }
}
