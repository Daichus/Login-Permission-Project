package login.permission.project.classes.repository;

import login.permission.project.classes.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for handling Permission entity database operations.
 */
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
}