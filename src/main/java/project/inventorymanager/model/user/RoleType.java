package project.inventorymanager.model.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Data
@Table(name = "role_types")
public class RoleType implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name",
            nullable = false,
            unique = true,
            columnDefinition = "varchar")
    @Enumerated(EnumType.STRING)
    private RoleName name;

    @Override
    public String getAuthority() {
        return name.name();
    }

    public enum RoleName {
        // Roles should be listed in ascending order of priority
        USER,
        EMPLOYEE,
        ADMIN;

        public static List<RoleName> getRolesUpTo(RoleName highestRole) {
            List<RoleName> rolesUpTo = new ArrayList<>();
            for (RoleName role : RoleName.values()) {
                rolesUpTo.add(role);
                if (role.equals(highestRole)) {
                    break;
                }
            }
            return rolesUpTo;
        }
    }
}
