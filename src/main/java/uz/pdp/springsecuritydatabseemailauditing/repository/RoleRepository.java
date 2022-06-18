package uz.pdp.springsecuritydatabseemailauditing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.springsecuritydatabseemailauditing.entity.Role;
import uz.pdp.springsecuritydatabseemailauditing.entity.enums.RoleName;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role findByRoleName(RoleName roleName);
}
