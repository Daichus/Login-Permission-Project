package login.permission.project.classes.repository;

import login.permission.project.classes.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role,Integer> {

    @Query("SELECT MAX(CAST(r.role_id AS int)) FROM Role r")
    Integer findMaxRoleId();

}
